package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class IDRange(
    max: Long,
    min: Long
)

object IDRange {
  implicit val `io.k8s.api.extensions.v1beta1.IDRange-Decoder`
      : Decoder[IDRange] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.IDRange-Encoder`
      : Encoder[IDRange] = deriveEncoder
}
