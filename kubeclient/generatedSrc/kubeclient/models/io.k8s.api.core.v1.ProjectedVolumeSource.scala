package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ProjectedVolumeSource(
    defaultMode: Option[Int] = None,
    sources: List[VolumeProjection]
)

object ProjectedVolumeSource {
  implicit val `io.k8s.api.core.v1.ProjectedVolumeSource-Decoder`
      : Decoder[ProjectedVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ProjectedVolumeSource-Encoder`
      : Encoder[ProjectedVolumeSource] = deriveEncoder
}
