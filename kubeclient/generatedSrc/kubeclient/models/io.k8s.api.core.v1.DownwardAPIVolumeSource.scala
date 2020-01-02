package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DownwardAPIVolumeSource(
    defaultMode: Option[Int] = None,
    items: Option[List[DownwardAPIVolumeFile]] = None
)

object DownwardAPIVolumeSource {
  implicit val `io.k8s.api.core.v1.DownwardAPIVolumeSource-Decoder`
      : Decoder[DownwardAPIVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.DownwardAPIVolumeSource-Encoder`
      : Encoder[DownwardAPIVolumeSource] = deriveEncoder
}
