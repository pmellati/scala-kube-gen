package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CSIVolumeSource(
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    driver: String,
    volumeAttributes: Option[Map[String, String]] = None,
    nodePublishSecretRef: Option[LocalObjectReference] = None
)

object CSIVolumeSource {
  implicit val `io.k8s.api.core.v1.CSIVolumeSource-Decoder`
      : Decoder[CSIVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.CSIVolumeSource-Encoder`
      : Encoder[CSIVolumeSource] = deriveEncoder
}
