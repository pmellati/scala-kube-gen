package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeList(
    apiVersion: Option[String] = None,
    items: List[Node],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object NodeList {
  implicit val `io.k8s.api.core.v1.NodeList-Decoder`: Decoder[NodeList] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeList-Encoder`: Encoder[NodeList] =
    deriveEncoder
}
