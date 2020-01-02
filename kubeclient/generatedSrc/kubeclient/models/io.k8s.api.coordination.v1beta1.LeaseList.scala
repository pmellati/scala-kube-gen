package kubeclient.io.k8s.api.coordination.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LeaseList(
    apiVersion: Option[String] = None,
    items: List[Lease],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object LeaseList {
  implicit val `io.k8s.api.coordination.v1beta1.LeaseList-Decoder`
      : Decoder[LeaseList] = deriveDecoder
  implicit val `io.k8s.api.coordination.v1beta1.LeaseList-Encoder`
      : Encoder[LeaseList] = deriveEncoder
}
