package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodReadinessGate(
    conditionType: String
)

object PodReadinessGate {
  implicit val `io.k8s.api.core.v1.PodReadinessGate-Decoder`
      : Decoder[PodReadinessGate] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PodReadinessGate-Encoder`
      : Encoder[PodReadinessGate] = deriveEncoder
}
