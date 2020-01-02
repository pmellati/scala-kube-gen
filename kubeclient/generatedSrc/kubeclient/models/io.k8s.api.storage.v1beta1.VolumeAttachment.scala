package kubeclient.io.k8s.api.storage.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeAttachment(
    status: Option[VolumeAttachmentStatus] = None,
    spec: VolumeAttachmentSpec,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object VolumeAttachment {
  implicit val `io.k8s.api.storage.v1beta1.VolumeAttachment-Decoder`
      : Decoder[VolumeAttachment] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.VolumeAttachment-Encoder`
      : Encoder[VolumeAttachment] = deriveEncoder
}
