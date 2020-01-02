package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeSpec(
    externalID: Option[String] = None,
    taints: Option[List[Taint]] = None,
    configSource: Option[NodeConfigSource] = None,
    unschedulable: Option[Boolean] = None,
    podCIDRs: Option[List[String]] = None,
    podCIDR: Option[String] = None,
    providerID: Option[String] = None
)

object NodeSpec {
  implicit val `io.k8s.api.core.v1.NodeSpec-Decoder`: Decoder[NodeSpec] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeSpec-Encoder`: Encoder[NodeSpec] =
    deriveEncoder
}
