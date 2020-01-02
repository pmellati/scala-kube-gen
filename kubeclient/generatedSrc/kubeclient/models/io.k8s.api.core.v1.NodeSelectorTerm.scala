package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeSelectorTerm(
    matchExpressions: Option[List[NodeSelectorRequirement]] = None,
    matchFields: Option[List[NodeSelectorRequirement]] = None
)

object NodeSelectorTerm {
  implicit val `io.k8s.api.core.v1.NodeSelectorTerm-Decoder`
      : Decoder[NodeSelectorTerm] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeSelectorTerm-Encoder`
      : Encoder[NodeSelectorTerm] = deriveEncoder
}
