package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PortworxVolumeSource(
    fsType: Option[String] = None,
    readOnly: Option[Boolean] = None,
    volumeID: String
)

object PortworxVolumeSource {
  implicit val `io.k8s.api.core.v1.PortworxVolumeSource-Decoder`
      : Decoder[PortworxVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PortworxVolumeSource-Encoder`
      : Encoder[PortworxVolumeSource] = deriveEncoder
}
