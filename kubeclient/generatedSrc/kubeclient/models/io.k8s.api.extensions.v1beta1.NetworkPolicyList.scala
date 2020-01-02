package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NetworkPolicyList(
    apiVersion: Option[String] = None,
    items: List[NetworkPolicy],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object NetworkPolicyList {
  implicit val `io.k8s.api.extensions.v1beta1.NetworkPolicyList-Decoder`
      : Decoder[NetworkPolicyList] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.NetworkPolicyList-Encoder`
      : Encoder[NetworkPolicyList] = deriveEncoder
}
