package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceRequirements(
    limits: Option[Map[String, Quantity]] = None,
    requests: Option[Map[String, Quantity]] = None
)

object ResourceRequirements {
  implicit val `io.k8s.api.core.v1.ResourceRequirements-Decoder`
      : Decoder[ResourceRequirements] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ResourceRequirements-Encoder`
      : Encoder[ResourceRequirements] = deriveEncoder
}
