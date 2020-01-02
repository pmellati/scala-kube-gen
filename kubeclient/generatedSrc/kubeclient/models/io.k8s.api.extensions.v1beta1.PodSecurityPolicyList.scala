package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodSecurityPolicyList(
    apiVersion: Option[String] = None,
    items: List[PodSecurityPolicy],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object PodSecurityPolicyList {
  implicit val `io.k8s.api.extensions.v1beta1.PodSecurityPolicyList-Decoder`
      : Decoder[PodSecurityPolicyList] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.PodSecurityPolicyList-Encoder`
      : Encoder[PodSecurityPolicyList] = deriveEncoder
}
