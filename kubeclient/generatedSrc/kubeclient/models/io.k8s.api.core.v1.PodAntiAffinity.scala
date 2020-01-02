package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodAntiAffinity(
    preferredDuringSchedulingIgnoredDuringExecution: Option[
      List[WeightedPodAffinityTerm]
    ] = None,
    requiredDuringSchedulingIgnoredDuringExecution: Option[
      List[PodAffinityTerm]
    ] = None
)

object PodAntiAffinity {
  implicit val `io.k8s.api.core.v1.PodAntiAffinity-Decoder`
      : Decoder[PodAntiAffinity] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PodAntiAffinity-Encoder`
      : Encoder[PodAntiAffinity] = deriveEncoder
}
