package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DownwardAPIProjection(
    items: Option[List[DownwardAPIVolumeFile]] = None
)

object DownwardAPIProjection {
  implicit val `io.k8s.api.core.v1.DownwardAPIProjection-Decoder`
      : Decoder[DownwardAPIProjection] = deriveDecoder
  implicit val `io.k8s.api.core.v1.DownwardAPIProjection-Encoder`
      : Encoder[DownwardAPIProjection] = deriveEncoder
}
