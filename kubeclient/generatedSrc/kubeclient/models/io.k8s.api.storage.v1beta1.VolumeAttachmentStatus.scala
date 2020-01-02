package kubeclient.io.k8s.api.storage.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeAttachmentStatus(
    attachError: Option[VolumeError] = None,
    attached: Boolean,
    attachmentMetadata: Option[Map[String, String]] = None,
    detachError: Option[VolumeError] = None
)

object VolumeAttachmentStatus {
  implicit val `io.k8s.api.storage.v1beta1.VolumeAttachmentStatus-Decoder`
      : Decoder[VolumeAttachmentStatus] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.VolumeAttachmentStatus-Encoder`
      : Encoder[VolumeAttachmentStatus] = deriveEncoder
}
