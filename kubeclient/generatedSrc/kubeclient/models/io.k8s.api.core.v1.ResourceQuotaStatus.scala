package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceQuotaStatus(
    hard: Option[Map[String, Quantity]] = None,
    used: Option[Map[String, Quantity]] = None
)

object ResourceQuotaStatus {
  implicit val `io.k8s.api.core.v1.ResourceQuotaStatus-Decoder`
      : Decoder[ResourceQuotaStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ResourceQuotaStatus-Encoder`
      : Encoder[ResourceQuotaStatus] = deriveEncoder
}
