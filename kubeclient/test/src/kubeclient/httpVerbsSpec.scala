package kubeclient

import java.util.UUID.randomUUID

import scala.concurrent.ExecutionContext.Implicits.global

import _root_.io.circe._, yaml.parser.{parse => parseYaml}
import cats.effect._
import org.specs2.mutable.Specification

import kubeclient.SslUtil.clientConfigFromKubeConfig

object httpVerbsSpec extends Specification {
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
}