package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodIP(
    ip: Option[String] = None
)

object PodIP {
  implicit val `io.k8s.api.core.v1.PodIP-Decoder`: Decoder[PodIP] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.PodIP-Encoder`: Encoder[PodIP] =
    deriveEncoder
}
