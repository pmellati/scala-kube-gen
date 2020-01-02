package kubeclient.io.k8s.api.networking.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NetworkPolicyIngressRule(
    from: Option[List[NetworkPolicyPeer]] = None,
    ports: Option[List[NetworkPolicyPort]] = None
)

object NetworkPolicyIngressRule {
  implicit val `io.k8s.api.networking.v1.NetworkPolicyIngressRule-Decoder`
      : Decoder[NetworkPolicyIngressRule] = deriveDecoder
  implicit val `io.k8s.api.networking.v1.NetworkPolicyIngressRule-Encoder`
      : Encoder[NetworkPolicyIngressRule] = deriveEncoder
}
