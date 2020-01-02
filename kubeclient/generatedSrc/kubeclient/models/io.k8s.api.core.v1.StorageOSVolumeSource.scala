package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StorageOSVolumeSource(
    volumeNamespace: Option[String] = None,
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    secretRef: Option[LocalObjectReference] = None,
    volumeName: Option[String] = None
)

object StorageOSVolumeSource {
  implicit val `io.k8s.api.core.v1.StorageOSVolumeSource-Decoder`
      : Decoder[StorageOSVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.StorageOSVolumeSource-Encoder`
      : Encoder[StorageOSVolumeSource] = deriveEncoder
}
