package kubeclient.io.k8s.api.apps.v1beta2

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Deployment(
    status: Option[DeploymentStatus] = None,
    spec: Option[DeploymentSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object Deployment {
  implicit val `io.k8s.api.apps.v1beta2.Deployment-Decoder`
      : Decoder[Deployment] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta2.Deployment-Encoder`
      : Encoder[Deployment] = deriveEncoder
}
