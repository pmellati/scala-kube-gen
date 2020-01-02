package kubeclient.io.k8s.api.storage.v1alpha1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeAttachmentSpec(
    attacher: String,
    nodeName: String,
    source: VolumeAttachmentSource
)

object VolumeAttachmentSpec {
  implicit val `io.k8s.api.storage.v1alpha1.VolumeAttachmentSpec-Decoder`
      : Decoder[VolumeAttachmentSpec] = deriveDecoder
  implicit val `io.k8s.api.storage.v1alpha1.VolumeAttachmentSpec-Encoder`
      : Encoder[VolumeAttachmentSpec] = deriveEncoder
}
