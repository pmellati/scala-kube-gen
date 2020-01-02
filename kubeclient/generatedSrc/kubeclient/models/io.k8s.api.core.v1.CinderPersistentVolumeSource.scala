package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CinderPersistentVolumeSource(
    fsType: Option[String] = None,
    readOnly: Option[Boolean] = None,
    secretRef: Option[SecretReference] = None,
    volumeID: String
)

object CinderPersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.CinderPersistentVolumeSource-Decoder`
      : Decoder[CinderPersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.CinderPersistentVolumeSource-Encoder`
      : Encoder[CinderPersistentVolumeSource] = deriveEncoder
}
