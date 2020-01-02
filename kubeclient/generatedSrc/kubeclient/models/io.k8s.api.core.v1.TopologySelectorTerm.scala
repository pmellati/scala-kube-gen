package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TopologySelectorTerm(
    matchLabelExpressions: Option[List[TopologySelectorLabelRequirement]] = None
)

object TopologySelectorTerm {
  implicit val `io.k8s.api.core.v1.TopologySelectorTerm-Decoder`
      : Decoder[TopologySelectorTerm] = deriveDecoder
  implicit val `io.k8s.api.core.v1.TopologySelectorTerm-Encoder`
      : Encoder[TopologySelectorTerm] = deriveEncoder
}
