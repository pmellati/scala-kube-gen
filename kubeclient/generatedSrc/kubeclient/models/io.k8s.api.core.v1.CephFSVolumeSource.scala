package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CephFSVolumeSource(
    secretRef: Option[LocalObjectReference] = None,
    secretFile: Option[String] = None,
    monitors: List[String],
    user: Option[String] = None,
    path: Option[String] = None,
    readOnly: Option[Boolean] = None
)

object CephFSVolumeSource {
  implicit val `io.k8s.api.core.v1.CephFSVolumeSource-Decoder`
      : Decoder[CephFSVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.CephFSVolumeSource-Encoder`
      : Encoder[CephFSVolumeSource] = deriveEncoder
}
