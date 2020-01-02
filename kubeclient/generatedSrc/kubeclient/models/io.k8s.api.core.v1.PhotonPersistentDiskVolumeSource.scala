package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PhotonPersistentDiskVolumeSource(
    fsType: Option[String] = None,
    pdID: String
)

object PhotonPersistentDiskVolumeSource {
  implicit val `io.k8s.api.core.v1.PhotonPersistentDiskVolumeSource-Decoder`
      : Decoder[PhotonPersistentDiskVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PhotonPersistentDiskVolumeSource-Encoder`
      : Encoder[PhotonPersistentDiskVolumeSource] = deriveEncoder
}
