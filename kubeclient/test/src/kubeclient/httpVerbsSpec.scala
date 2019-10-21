package kubeclient

import java.util.UUID.randomUUID

import cats.effect._
import io.circe._, yaml.parser.{parse => parseYaml}
import org.http4s.Uri
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

// import openapigen.ClientConfig
import myapi.CoreV1Api


// import scala.concurrent.ExecutionContext.Implicits.global

object httpVerbsSpec extends Specification with BeforeAfterAll {
  val kubeClusterName = randomUUID().toString

  // implicit val cs: ContextShift[IO] = IO.contextShift(global)

  override def beforeAll(): Unit = {
    os.proc("kind", "create", "cluster", "--name", kubeClusterName).call()

    // val kubeConfig = parseYaml(
    //   os.proc("kind", "get", "kubeconfig", "--name", kubeClusterName).call().out.trim
    // ).right.get

    // val cluster = kubeConfig.hcursor.downField("clusters").as[List[Json]].right.get.head

    // val kubeServerUri = cluster.hcursor.downField("cluster").downField("server").as[String].right.get

    // Uri.fromString(kubeServerUri).right.get
  }

  override def afterAll(): Unit = {
    os.proc("kind", "delete", "cluster", "--name", kubeClusterName).call()
  }

  "GET requests should work" in {
    val kubeServerUri = serverUri(kubeConfig())

    println(s"SERVER: $kubeServerUri")

    CoreV1Api.listCoreV1Node[IO]()
    pending
  }

  private def kubeConfig(): Json =
    parseYaml(
      os.proc("kind", "get", "kubeconfig", "--name", kubeClusterName).call().out.trim
    ).right.get

  private def serverUri(kubeConfig: Json): Uri = {
    val cluster = kubeConfig.hcursor.downField("clusters").as[List[Json]].right.get.head

    val kubeServerUri = cluster.hcursor.downField("cluster").downField("server").as[String].right.get

    Uri.fromString(kubeServerUri).right.get
  }
}