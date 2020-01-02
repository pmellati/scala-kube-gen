package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RBDVolumeSource(
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    image: String,
    secretRef: Option[LocalObjectReference] = None,
    pool: Option[String] = None,
    keyring: Option[String] = None,
    monitors: List[String],
    user: Option[String] = None
)

object RBDVolumeSource {
  implicit val `io.k8s.api.core.v1.RBDVolumeSource-Decoder`
      : Decoder[RBDVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.RBDVolumeSource-Encoder`
      : Encoder[RBDVolumeSource] = deriveEncoder
}
