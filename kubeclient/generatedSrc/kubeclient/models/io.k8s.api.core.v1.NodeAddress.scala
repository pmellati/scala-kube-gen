package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeAddress(
    address: String,
    `type`: String
)

object NodeAddress {
  implicit val `io.k8s.api.core.v1.NodeAddress-Decoder`: Decoder[NodeAddress] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeAddress-Encoder`: Encoder[NodeAddress] =
    deriveEncoder
}
