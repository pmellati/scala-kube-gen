package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NFSVolumeSource(
    path: String,
    readOnly: Option[Boolean] = None,
    server: String
)

object NFSVolumeSource {
  implicit val `io.k8s.api.core.v1.NFSVolumeSource-Decoder`
      : Decoder[NFSVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NFSVolumeSource-Encoder`
      : Encoder[NFSVolumeSource] = deriveEncoder
}
