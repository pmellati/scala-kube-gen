package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DownwardAPIVolumeFile(
    fieldRef: Option[ObjectFieldSelector] = None,
    mode: Option[Int] = None,
    path: String,
    resourceFieldRef: Option[ResourceFieldSelector] = None
)

object DownwardAPIVolumeFile {
  implicit val `io.k8s.api.core.v1.DownwardAPIVolumeFile-Decoder`
      : Decoder[DownwardAPIVolumeFile] = deriveDecoder
  implicit val `io.k8s.api.core.v1.DownwardAPIVolumeFile-Encoder`
      : Encoder[DownwardAPIVolumeFile] = deriveEncoder
}
