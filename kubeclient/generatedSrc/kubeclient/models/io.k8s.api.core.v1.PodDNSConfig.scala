package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodDNSConfig(
    nameservers: Option[List[String]] = None,
    options: Option[List[PodDNSConfigOption]] = None,
    searches: Option[List[String]] = None
)

object PodDNSConfig {
  implicit val `io.k8s.api.core.v1.PodDNSConfig-Decoder`
      : Decoder[PodDNSConfig] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PodDNSConfig-Encoder`
      : Encoder[PodDNSConfig] = deriveEncoder
}
