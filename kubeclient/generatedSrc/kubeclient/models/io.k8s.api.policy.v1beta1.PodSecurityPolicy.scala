package kubeclient.io.k8s.api.policy.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodSecurityPolicy(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    spec: Option[PodSecurityPolicySpec] = None
)

object PodSecurityPolicy {
  implicit val `io.k8s.api.policy.v1beta1.PodSecurityPolicy-Decoder`
      : Decoder[PodSecurityPolicy] = deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.PodSecurityPolicy-Encoder`
      : Encoder[PodSecurityPolicy] = deriveEncoder
}
