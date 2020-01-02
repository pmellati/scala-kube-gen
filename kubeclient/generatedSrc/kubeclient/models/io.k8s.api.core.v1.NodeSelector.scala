package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeSelector(
    nodeSelectorTerms: List[NodeSelectorTerm]
)

object NodeSelector {
  implicit val `io.k8s.api.core.v1.NodeSelector-Decoder`
      : Decoder[NodeSelector] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeSelector-Encoder`
      : Encoder[NodeSelector] = deriveEncoder
}
