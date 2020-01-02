package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class GlusterfsVolumeSource(
    endpoints: String,
    path: String,
    readOnly: Option[Boolean] = None
)

object GlusterfsVolumeSource {
  implicit val `io.k8s.api.core.v1.GlusterfsVolumeSource-Decoder`
      : Decoder[GlusterfsVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.GlusterfsVolumeSource-Encoder`
      : Encoder[GlusterfsVolumeSource] = deriveEncoder
}
