package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeDaemonEndpoints(
    kubeletEndpoint: Option[DaemonEndpoint] = None
)

object NodeDaemonEndpoints {
  implicit val `io.k8s.api.core.v1.NodeDaemonEndpoints-Decoder`
      : Decoder[NodeDaemonEndpoints] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeDaemonEndpoints-Encoder`
      : Encoder[NodeDaemonEndpoints] = deriveEncoder
}
