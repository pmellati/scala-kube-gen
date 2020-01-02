package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CinderVolumeSource(
    fsType: Option[String] = None,
    readOnly: Option[Boolean] = None,
    secretRef: Option[LocalObjectReference] = None,
    volumeID: String
)

object CinderVolumeSource {
  implicit val `io.k8s.api.core.v1.CinderVolumeSource-Decoder`
      : Decoder[CinderVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.CinderVolumeSource-Encoder`
      : Encoder[CinderVolumeSource] = deriveEncoder
}
