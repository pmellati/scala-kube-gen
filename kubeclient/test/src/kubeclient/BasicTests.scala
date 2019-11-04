package kubeclient

import java.util.UUID.randomUUID

import scala.concurrent.ExecutionContext.Implicits.global

import _root_.io.circe._, yaml.parser.{parse => parseYaml}
import cats.effect._
import org.specs2.execute.{AsResult, Result}
import org.specs2.mutable.Specification

import kubeclient.CoreV1Api.Action    // TODO: this shouldn't be defined per API.
import kubeclient.io.k8s.api.apps.v1.Deployment
import kubeclient.io.k8s.api.core.v1.Namespace
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta
import kubeclient.SslUtil.clientConfigFromKubeConfig

object BasicTests extends Specification {
  implicit val cs: ContextShift[IO] = IO.contextShift(global)

  val clientConfig = clientConfigFromKubeConfig[IO](readTestKubeConf())

  "Create, fetch & delete a namespace" in tempNs { nsName =>
    val ns = run(CoreV1Api.readCoreV1Namespace[IO](nsName))

    ns.apiVersion        must beSome("v1")
    ns.kind              must beSome("Namespace")
    ns.metadata.get.name must beSome(nsName)
  }

  "Create, fetch & delete a deployment" in tempNs { ns =>
    val deploymentYaml = """
      apiVersion: apps/v1
      kind: Deployment
      metadata:
        name: nginx-deployment
        labels:
          app: nginx
      spec:
        replicas: 3
        selector:
          matchLabels:
            app: nginx
        template:
          metadata:
            labels:
              app: nginx
          spec:
            containers:
            - name: nginx
              image: nginx:1.7.9
              ports:
              - containerPort: 80
    """

    val deployment = parseYaml(deploymentYaml).right.get.as[Deployment].right.get

    val fetched = run(
      for {
        _       <- AppsV1Api.createAppsV1NamespacedDeployment[IO](namespace = ns, body = deployment)
        fetched <- AppsV1Api.readAppsV1NamespacedDeployment[IO](name = "nginx-deployment", namespace = ns)
        _       <- AppsV1Api.deleteAppsV1NamespacedDeployment[IO](name = "nginx-deployment", namespace = ns)
      } yield fetched
    )

    fetched.spec.get.replicas.get must_== 3
    fetched.spec.get.selector.matchLabels.get must_== Map("app" -> "nginx")
    fetched.spec.get.template.metadata.get.labels.get must_== Map("app" -> "nginx")
    fetched.spec.get.template.spec.get.containers.length must_== 1
    fetched.spec.get.template.spec.get.containers.head.name must_== "nginx"
    fetched.spec.get.template.spec.get.containers.head.image.get must_== "nginx:1.7.9"
    fetched.spec.get.template.spec.get.containers.head.command must beNone
    fetched.spec.get.template.spec.get.containers.head.ports.get.head.containerPort must_== 80
  }

  private def run[A](action: Action[IO, A]): A =
    clientConfig.use(action.run).unsafeRunSync()

  /** Testing helper to run a test with a temporary namespace that will be deleted after the test. */
  private def tempNs[R : AsResult](runTest: String => R): Result = {
    val nsName = randomUUID().toString

    val ns = Namespace(
      metadata = Some(ObjectMeta(
        name = Some(nsName))
      )
    )

    run(
      for {
        _          <- CoreV1Api.createCoreV1Namespace[IO](body = ns)
        testResult  = AsResult(runTest(nsName))
        _          <- CoreV1Api.deleteCoreV1Namespace[IO](nsName)
      } yield testResult
    )
  }

  private def readTestKubeConf(): Json = {
    val kubeConfStr = scala.io.Source.fromFile(".tmp/test-kube-conf").mkString
    parseYaml(kubeConfStr).right.get
  }
}