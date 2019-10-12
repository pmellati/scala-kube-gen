package example

import cats.effect._

import io.k8s.api.core.v1.Node
import myapi.CoreV1Api.readCoreV1NodeStatus

import org.http4s.client.blaze._
import org.http4s._, client._
import org.http4s.circe._
import io.circe.Json

import openapigen.ClientConfig

import scala.concurrent.ExecutionContext.Implicits.global
import org.http4s.headers.Authorization
import org.http4s.parser.AuthorizationHeader

object Main {
  val apiBaseUri = Uri.uri("https://localhost:6443")

  implicit val cs: ContextShift[IO] = IO.contextShift(global)

  def addBearerToken[F[_]](token: String, client: Client[F])(
      implicit F: Bracket[F, Throwable]
  ): Client[F] = Client[F] { req =>
    val requestWithToken = req.withHeaders(
      Authorization(Credentials.Token(AuthScheme.Bearer, KubeBearerToken))
    )

    client.run(requestWithToken)
  }

  def main(args: Array[String]): Unit = {
    val noTlsVerifyClientBuilder = BlazeClientBuilder[IO](global)
      .withSslContextOption(BlazeClientConfig.insecure.sslContext)
      .withCheckEndpointAuthentication(false)

    val node = noTlsVerifyClientBuilder.resource
      .use { client =>
        val authenticatingClient = addBearerToken(KubeBearerToken, client)
        val clientConfig         = ClientConfig(authenticatingClient, apiBaseUri)
        readCoreV1NodeStatus[IO]("docker-for-desktop").run(clientConfig)
      }
      .unsafeRunSync()

    println(s"NODE: $node")
  }

  final val KubeBearerToken =
    """eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InNjYWxhLWFwaS10b2tlbi1tcXM3dyIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJzY2FsYS1hcGkiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiIwZTk4OGU4Mi04NDY4LTExZTktYmIyYS0wMjUwMDAwMDAwMDEiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGVmYXVsdDpzY2FsYS1hcGkifQ.Ck6GM03t5scEXfVbuW8hbjhb0GfhGI71Z3FUStmwNoOdXEyfL25NWHK0KEfpaxXbZ5eIsFyaVnw9dfuRSHmx0kxojHwMhoctKeo4XQp_UZYfMCFYMbNWOSeLYZ7OYCkbugKZsqyF8svZPg6xhWpUGXF2EuoSrvbWMeCbgwlI_vmB2FHNmAJ9XXMRm4pok_T-Ny32HupjUJt-C5NGplf4YUMwecgXjWqyrA0_aO1tEZeHnzNBzBWM0OnB3Bcqlnq98tXlSFqu9nQd2RR_dGos1mQ6a6rXy3aQJ13zpyGkqLe8o4nNp-KtCstV1dGLE3SpuQlMps-cHxD9oXUlaP6czw"""
}
