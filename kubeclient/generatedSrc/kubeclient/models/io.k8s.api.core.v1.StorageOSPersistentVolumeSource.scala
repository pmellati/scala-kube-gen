package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StorageOSPersistentVolumeSource(
    volumeNamespace: Option[String] = None,
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    secretRef: Option[ObjectReference] = None,
    volumeName: Option[String] = None
)

object StorageOSPersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.StorageOSPersistentVolumeSource-Decoder`
      : Decoder[StorageOSPersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.StorageOSPersistentVolumeSource-Encoder`
      : Encoder[StorageOSPersistentVolumeSource] = deriveEncoder
}
