package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PersistentVolumeClaimVolumeSource(
    claimName: String,
    readOnly: Option[Boolean] = None
)

object PersistentVolumeClaimVolumeSource {
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimVolumeSource-Decoder`
      : Decoder[PersistentVolumeClaimVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimVolumeSource-Encoder`
      : Encoder[PersistentVolumeClaimVolumeSource] = deriveEncoder
}
