package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class WeightedPodAffinityTerm(
    podAffinityTerm: PodAffinityTerm,
    weight: Int
)

object WeightedPodAffinityTerm {
  implicit val `io.k8s.api.core.v1.WeightedPodAffinityTerm-Decoder`
      : Decoder[WeightedPodAffinityTerm] = deriveDecoder
  implicit val `io.k8s.api.core.v1.WeightedPodAffinityTerm-Encoder`
      : Encoder[WeightedPodAffinityTerm] = deriveEncoder
}
