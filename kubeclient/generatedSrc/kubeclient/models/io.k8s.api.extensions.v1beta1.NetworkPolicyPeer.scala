package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NetworkPolicyPeer(
    ipBlock: Option[IPBlock] = None,
    namespaceSelector: Option[LabelSelector] = None,
    podSelector: Option[LabelSelector] = None
)

object NetworkPolicyPeer {
  implicit val `io.k8s.api.extensions.v1beta1.NetworkPolicyPeer-Decoder`
      : Decoder[NetworkPolicyPeer] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.NetworkPolicyPeer-Encoder`
      : Encoder[NetworkPolicyPeer] = deriveEncoder
}
