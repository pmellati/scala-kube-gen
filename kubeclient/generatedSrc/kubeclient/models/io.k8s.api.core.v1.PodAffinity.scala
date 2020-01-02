package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodAffinity(
    preferredDuringSchedulingIgnoredDuringExecution: Option[
      List[WeightedPodAffinityTerm]
    ] = None,
    requiredDuringSchedulingIgnoredDuringExecution: Option[
      List[PodAffinityTerm]
    ] = None
)

object PodAffinity {
  implicit val `io.k8s.api.core.v1.PodAffinity-Decoder`: Decoder[PodAffinity] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.PodAffinity-Encoder`: Encoder[PodAffinity] =
    deriveEncoder
}
