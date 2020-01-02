package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class GCEPersistentDiskVolumeSource(
    fsType: Option[String] = None,
    partition: Option[Int] = None,
    pdName: String,
    readOnly: Option[Boolean] = None
)

object GCEPersistentDiskVolumeSource {
  implicit val `io.k8s.api.core.v1.GCEPersistentDiskVolumeSource-Decoder`
      : Decoder[GCEPersistentDiskVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.GCEPersistentDiskVolumeSource-Encoder`
      : Encoder[GCEPersistentDiskVolumeSource] = deriveEncoder
}
