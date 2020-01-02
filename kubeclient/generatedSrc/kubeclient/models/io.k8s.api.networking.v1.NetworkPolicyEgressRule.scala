package kubeclient.io.k8s.api.networking.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NetworkPolicyEgressRule(
    ports: Option[List[NetworkPolicyPort]] = None,
    to: Option[List[NetworkPolicyPeer]] = None
)

object NetworkPolicyEgressRule {
  implicit val `io.k8s.api.networking.v1.NetworkPolicyEgressRule-Decoder`
      : Decoder[NetworkPolicyEgressRule] = deriveDecoder
  implicit val `io.k8s.api.networking.v1.NetworkPolicyEgressRule-Encoder`
      : Encoder[NetworkPolicyEgressRule] = deriveEncoder
}
