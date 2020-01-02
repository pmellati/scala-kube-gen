package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Node(
    status: Option[NodeStatus] = None,
    spec: Option[NodeSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object Node {
  implicit val `io.k8s.api.core.v1.Node-Decoder`: Decoder[Node] = deriveDecoder
  implicit val `io.k8s.api.core.v1.Node-Encoder`: Encoder[Node] = deriveEncoder
}
