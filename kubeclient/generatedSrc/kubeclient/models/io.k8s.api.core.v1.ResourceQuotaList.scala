package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceQuotaList(
    apiVersion: Option[String] = None,
    items: List[ResourceQuota],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ResourceQuotaList {
  implicit val `io.k8s.api.core.v1.ResourceQuotaList-Decoder`
      : Decoder[ResourceQuotaList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ResourceQuotaList-Encoder`
      : Encoder[ResourceQuotaList] = deriveEncoder
}
