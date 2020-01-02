package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DeploymentList(
    apiVersion: Option[String] = None,
    items: List[Deployment],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object DeploymentList {
  implicit val `io.k8s.api.extensions.v1beta1.DeploymentList-Decoder`
      : Decoder[DeploymentList] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.DeploymentList-Encoder`
      : Encoder[DeploymentList] = deriveEncoder
}
