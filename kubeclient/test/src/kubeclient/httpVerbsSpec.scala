package kubeclient

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets.UTF_8
import java.security._, spec._
import java.security.cert.{Certificate, CertificateFactory, X509Certificate}
import java.util.Base64
import java.util.UUID.randomUUID
import javax.net.ssl.{SSLContext, KeyManagerFactory, TrustManagerFactory}

import scala.concurrent.ExecutionContext.Implicits.global

import cats.effect._
import io.circe._, yaml.parser.{parse => parseYaml}
import org.http4s.client.blaze._
import org.http4s.Uri
import org.specs2.mutable.Specification

import openapigen.ClientConfig
import myapi.CoreV1Api

object httpVerbsSpec extends Specification /*with BeforeAfterAll*/ {
  val kubeClusterName = randomUUID().toString

  implicit val cs: ContextShift[IO] = IO.contextShift(global)

  "GET requests should work" in {
    val clientConfig = clientConfigFromKubeConfig[IO](kubeConfigFromEnv())

    val nodes = clientConfig.use { clientConfig =>
      CoreV1Api.listCoreV1Node[IO]().run(clientConfig)
    }.unsafeRunSync()

    nodes.items must_== Nil
  }

  private def kubeConfigFromEnv(): Json =
    parseYaml(sys.env("TEST_KUBE_CONF")).right.get

  /** Note: very fragile function. Will not work with all kubeconfigs. Will probably work with kubeconfigs that contain
   *  client key & cert and a server ca cert.
   */
  def clientConfigFromKubeConfig[F[_]: ConcurrentEffect](kubeConfig: Json): Resource[F, ClientConfig[F]] = {
    val (sslContext, apiUri) = sslContextAndApiUriFromKubeConfig(kubeConfig)

    BlazeClientBuilder[F](global)
      .withSslContext(sslContext)
      .resource
      .map { http4sClient =>
        ClientConfig(http4sClient, apiUri)
      }
  }

  /** Note: very fragile function. Will not work with all kubeconfigs. Will probably work with kubeconfigs that contain
   *  client key & cert and a server ca cert.
   */
  def sslContextAndApiUriFromKubeConfig(kubeConfig: Json): (SSLContext, Uri) = {
    val currentContext = kubeConfig.hcursor.downField("current-context").as[String].right.get
    val Array(username, clusterName) = currentContext.split("@")

    val user: Json = kubeConfig.hcursor.downField("users").as[List[Json]].right.get.find(_.hcursor.downField("name").as[String] == Right(username)).get

    val clientKeyBase64Encoded = user.hcursor.downField("user").downField("client-key-data").as[String].right.get

    val clientCertBase64Encoded = user.hcursor.downField("user").downField("client-certificate-data").as[String].right.get

    val cluster: Json = kubeConfig.hcursor.downField("clusters").as[List[Json]].right.get.find(_.hcursor.downField("name").as[String] == Right(clusterName)).get

    val clusterCaCertBase64Encoded = cluster.hcursor.downField("cluster").downField("certificate-authority-data").as[String].right.get

    val clientKeyPem  = base64DecodedStr(clientKeyBase64Encoded)
    val clientCertPem = base64DecodedStr(clientCertBase64Encoded)
    val caCertPem     = base64DecodedStr(clusterCaCertBase64Encoded)

    val sslCtxt = sslContext(keyPem = clientKeyPem, certChainPems = Array(clientCertPem), caCertPem = caCertPem)

    val clusterUriStr = cluster.hcursor.downField("cluster").downField("server").as[String].right.get

    val clusterUri = Uri.fromString(clusterUriStr).right.get

    (sslCtxt, clusterUri)
  }

  /** Ensure `certChain` does not include the root `caCert`. */
  def sslContext(keyPem: String, certChainPems: Array[String], caCertPem: String): SSLContext = {
    val key: PrivateKey = readPemPrivateKey(keyPem)
    val certs: Array[Certificate] = certChainPems.map(readPemCert)
    val ca: Certificate = readPemCert(caCertPem)

    sslContext(key, certs, ca)
  }

  /** Ensure `certChain` does not include the root `caCert`. */
  def sslContext(key: PrivateKey, certChain: Array[Certificate], caCert: Certificate): SSLContext = {
    val sslContext = SSLContext.getInstance("TLSv1.2")

    val kmf = clientAuthKeyManagerFactory(
      key = key,
      certChain = certChain :+ caCert
    )

    val tmf = peerTrustManagerFactory(caCert)

    sslContext.init(kmf.getKeyManagers, tmf.getTrustManagers, new SecureRandom());

    sslContext
  }

  def clientAuthKeyManagerFactory(key: PrivateKey, certChain: Array[Certificate]): KeyManagerFactory = {
    val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType)
    keyStore.load(null)
    keyStore.setKeyEntry("key", key, Array.empty, certChain)

    val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    kmf.init(keyStore, Array.empty[Char])
    kmf
  }

  def peerTrustManagerFactory(ca: Certificate): TrustManagerFactory = {
    val ksT: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType)
    ksT.load(null)
    ksT.setCertificateEntry("ca", ca)

    val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    tmf.init(ksT)
    tmf
  }

  def readPemCert(certPem: String): X509Certificate =
    CertificateFactory
      .getInstance("X509")
      .generateCertificate(
        byteStream(certPem)
      ).asInstanceOf[X509Certificate]

  /** Can read PKCS1 & PKCS8 pem-encoded private keys. */
  def readPemPrivateKey(keyPem: String): PrivateKey = {
    val kf = KeyFactory.getInstance("RSA")

    try {
      kf.generatePrivate(new PKCS8EncodedKeySpec(keyPem.getBytes("UTF-8")))
    } catch { case _: InvalidKeySpecException =>
      val pkcs1Bytes = pemToDer(keyPem)
      val pkcs8Bytes = pkcs1PrivateKeyToPkcs8(pkcs1Bytes)

      kf.generatePrivate(new PKCS8EncodedKeySpec(pkcs8Bytes))
    }
  }

  def byteStream(s: String) = new ByteArrayInputStream(s.getBytes("UTF-8"))

  def base64DecodedBytes(encodedData: String): Array[Byte] = {
    val singleLine = encodedData.split("\n").mkString
    Base64.getDecoder().decode(singleLine)
  }

  def base64DecodedStr(encoded: String): String =
    new String(base64DecodedBytes(encoded), UTF_8)

  def pemToDer(pemStr: String): Array[Byte] =
    Base64.getDecoder().decode(
      pemStr.split("\n").tail.init.mkString
    )

  /** Adapted from https://github.com/Mastercard/client-encryption-java/blob/000d7ffe12599d788bb012087f4acb8cb0a8f291/src/main/java/com/mastercard/developer/utils/EncryptionUtils.java
   *  Licensed under the MIT license: https://github.com/Mastercard/client-encryption-java/blob/000d7ffe12599d788bb012087f4acb8cb0a8f291/LICENSE
   */
  def pkcs1PrivateKeyToPkcs8(pkcs1Bytes: Array[Byte]): Array[Byte] = {
    val pkcs1Length = pkcs1Bytes.length
    val totalLength = pkcs1Length + 22

    val pkcs8Header = Array(
      0x30, 0x82, ((totalLength >> 8) & 0xff), (totalLength & 0xff), // Sequence + total length
      0x2, 0x1, 0x0, // Integer (0)
      0x30, 0xD, 0x6, 0x9, 0x2A, 0x86, 0x48, 0x86, 0xF7, 0xD, 0x1, 0x1, 0x1, 0x5, 0x0, // Sequence: 1.2.840.113549.1.1.1, NULL
      0x4, 0x82, ((pkcs1Length >> 8) & 0xff), (pkcs1Length & 0xff) // Octet string + length
    ).map(_.toByte)

    pkcs8Header ++ pkcs1Bytes
  }
}