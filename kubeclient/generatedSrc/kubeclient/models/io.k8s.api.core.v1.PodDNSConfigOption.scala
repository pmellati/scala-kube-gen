package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodDNSConfigOption(
    name: Option[String] = None,
    value: Option[String] = None
)

object PodDNSConfigOption {
  implicit val `io.k8s.api.core.v1.PodDNSConfigOption-Decoder`
      : Decoder[PodDNSConfigOption] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PodDNSConfigOption-Encoder`
      : Encoder[PodDNSConfigOption] = deriveEncoder
}
