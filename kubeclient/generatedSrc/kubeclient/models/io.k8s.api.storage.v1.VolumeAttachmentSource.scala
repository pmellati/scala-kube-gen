package kubeclient.io.k8s.api.storage.v1

import kubeclient.io.k8s.api.core.v1.PersistentVolumeSpec

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeAttachmentSource(
    inlineVolumeSpec: Option[PersistentVolumeSpec] = None,
    persistentVolumeName: Option[String] = None
)

object VolumeAttachmentSource {
  implicit val `io.k8s.api.storage.v1.VolumeAttachmentSource-Decoder`
      : Decoder[VolumeAttachmentSource] = deriveDecoder
  implicit val `io.k8s.api.storage.v1.VolumeAttachmentSource-Encoder`
      : Encoder[VolumeAttachmentSource] = deriveEncoder
}
