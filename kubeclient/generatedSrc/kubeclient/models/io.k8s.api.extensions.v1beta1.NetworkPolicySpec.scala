package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NetworkPolicySpec(
    egress: Option[List[NetworkPolicyEgressRule]] = None,
    ingress: Option[List[NetworkPolicyIngressRule]] = None,
    podSelector: LabelSelector,
    policyTypes: Option[List[String]] = None
)

object NetworkPolicySpec {
  implicit val `io.k8s.api.extensions.v1beta1.NetworkPolicySpec-Decoder`
      : Decoder[NetworkPolicySpec] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.NetworkPolicySpec-Encoder`
      : Encoder[NetworkPolicySpec] = deriveEncoder
}
