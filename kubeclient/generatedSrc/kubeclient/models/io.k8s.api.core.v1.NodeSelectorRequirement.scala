package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeSelectorRequirement(
    key: String,
    operator: String,
    values: Option[List[String]] = None
)

object NodeSelectorRequirement {
  implicit val `io.k8s.api.core.v1.NodeSelectorRequirement-Decoder`
      : Decoder[NodeSelectorRequirement] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeSelectorRequirement-Encoder`
      : Encoder[NodeSelectorRequirement] = deriveEncoder
}
