package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HostPortRange(
    max: Int,
    min: Int
)

object HostPortRange {
  implicit val `io.k8s.api.extensions.v1beta1.HostPortRange-Decoder`
      : Decoder[HostPortRange] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.HostPortRange-Encoder`
      : Encoder[HostPortRange] = deriveEncoder
}
