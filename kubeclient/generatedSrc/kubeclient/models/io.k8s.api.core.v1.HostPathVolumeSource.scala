package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HostPathVolumeSource(
    path: String,
    `type`: Option[String] = None
)

object HostPathVolumeSource {
  implicit val `io.k8s.api.core.v1.HostPathVolumeSource-Decoder`
      : Decoder[HostPathVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.HostPathVolumeSource-Encoder`
      : Encoder[HostPathVolumeSource] = deriveEncoder
}
