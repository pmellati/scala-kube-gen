package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PreferredSchedulingTerm(
    preference: NodeSelectorTerm,
    weight: Int
)

object PreferredSchedulingTerm {
  implicit val `io.k8s.api.core.v1.PreferredSchedulingTerm-Decoder`
      : Decoder[PreferredSchedulingTerm] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PreferredSchedulingTerm-Encoder`
      : Encoder[PreferredSchedulingTerm] = deriveEncoder
}
