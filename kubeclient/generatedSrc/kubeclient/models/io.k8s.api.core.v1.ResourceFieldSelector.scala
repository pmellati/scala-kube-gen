package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceFieldSelector(
    containerName: Option[String] = None,
    divisor: Option[Quantity] = None,
    resource: String
)

object ResourceFieldSelector {
  implicit val `io.k8s.api.core.v1.ResourceFieldSelector-Decoder`
      : Decoder[ResourceFieldSelector] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ResourceFieldSelector-Encoder`
      : Encoder[ResourceFieldSelector] = deriveEncoder
}
