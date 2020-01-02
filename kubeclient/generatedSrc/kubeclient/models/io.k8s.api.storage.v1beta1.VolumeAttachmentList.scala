package kubeclient.io.k8s.api.storage.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeAttachmentList(
    apiVersion: Option[String] = None,
    items: List[VolumeAttachment],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object VolumeAttachmentList {
  implicit val `io.k8s.api.storage.v1beta1.VolumeAttachmentList-Decoder`
      : Decoder[VolumeAttachmentList] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.VolumeAttachmentList-Encoder`
      : Encoder[VolumeAttachmentList] = deriveEncoder
}
