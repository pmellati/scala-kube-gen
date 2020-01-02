package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RBDPersistentVolumeSource(
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    image: String,
    secretRef: Option[SecretReference] = None,
    pool: Option[String] = None,
    keyring: Option[String] = None,
    monitors: List[String],
    user: Option[String] = None
)

object RBDPersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.RBDPersistentVolumeSource-Decoder`
      : Decoder[RBDPersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.RBDPersistentVolumeSource-Encoder`
      : Encoder[RBDPersistentVolumeSource] = deriveEncoder
}
