package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LimitRangeSpec(
    limits: List[LimitRangeItem]
)

object LimitRangeSpec {
  implicit val `io.k8s.api.core.v1.LimitRangeSpec-Decoder`
      : Decoder[LimitRangeSpec] = deriveDecoder
  implicit val `io.k8s.api.core.v1.LimitRangeSpec-Encoder`
      : Encoder[LimitRangeSpec] = deriveEncoder
}
