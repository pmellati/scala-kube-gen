package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceQuota(
    status: Option[ResourceQuotaStatus] = None,
    spec: Option[ResourceQuotaSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object ResourceQuota {
  implicit val `io.k8s.api.core.v1.ResourceQuota-Decoder`
      : Decoder[ResourceQuota] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ResourceQuota-Encoder`
      : Encoder[ResourceQuota] = deriveEncoder
}
