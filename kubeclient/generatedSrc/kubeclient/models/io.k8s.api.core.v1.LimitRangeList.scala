package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LimitRangeList(
    apiVersion: Option[String] = None,
    items: List[LimitRange],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object LimitRangeList {
  implicit val `io.k8s.api.core.v1.LimitRangeList-Decoder`
      : Decoder[LimitRangeList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.LimitRangeList-Encoder`
      : Encoder[LimitRangeList] = deriveEncoder
}
