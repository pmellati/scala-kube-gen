package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LocalVolumeSource(
    fsType: Option[String] = None,
    path: String
)

object LocalVolumeSource {
  implicit val `io.k8s.api.core.v1.LocalVolumeSource-Decoder`
      : Decoder[LocalVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.LocalVolumeSource-Encoder`
      : Encoder[LocalVolumeSource] = deriveEncoder
}
