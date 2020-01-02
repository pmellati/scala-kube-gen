package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeCondition(
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    lastHeartbeatTime: Option[Time] = None,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object NodeCondition {
  implicit val `io.k8s.api.core.v1.NodeCondition-Decoder`
      : Decoder[NodeCondition] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeCondition-Encoder`
      : Encoder[NodeCondition] = deriveEncoder
}
