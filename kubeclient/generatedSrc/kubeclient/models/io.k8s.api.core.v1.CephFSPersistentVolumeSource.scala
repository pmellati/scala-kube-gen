package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CephFSPersistentVolumeSource(
    secretRef: Option[SecretReference] = None,
    secretFile: Option[String] = None,
    monitors: List[String],
    user: Option[String] = None,
    path: Option[String] = None,
    readOnly: Option[Boolean] = None
)

object CephFSPersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.CephFSPersistentVolumeSource-Decoder`
      : Decoder[CephFSPersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.CephFSPersistentVolumeSource-Encoder`
      : Encoder[CephFSPersistentVolumeSource] = deriveEncoder
}
