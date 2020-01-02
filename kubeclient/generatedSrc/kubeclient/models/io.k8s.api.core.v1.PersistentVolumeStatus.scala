package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PersistentVolumeStatus(
    message: Option[String] = None,
    phase: Option[String] = None,
    reason: Option[String] = None
)

object PersistentVolumeStatus {
  implicit val `io.k8s.api.core.v1.PersistentVolumeStatus-Decoder`
      : Decoder[PersistentVolumeStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PersistentVolumeStatus-Encoder`
      : Encoder[PersistentVolumeStatus] = deriveEncoder
}
