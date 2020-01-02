package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeConfigStatus(
    active: Option[NodeConfigSource] = None,
    assigned: Option[NodeConfigSource] = None,
    error: Option[String] = None,
    lastKnownGood: Option[NodeConfigSource] = None
)

object NodeConfigStatus {
  implicit val `io.k8s.api.core.v1.NodeConfigStatus-Decoder`
      : Decoder[NodeConfigStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeConfigStatus-Encoder`
      : Encoder[NodeConfigStatus] = deriveEncoder
}
