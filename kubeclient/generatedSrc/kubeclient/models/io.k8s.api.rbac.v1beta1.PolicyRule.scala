package kubeclient.io.k8s.api.rbac.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PolicyRule(
    verbs: List[String],
    apiGroups: Option[List[String]] = None,
    resources: Option[List[String]] = None,
    resourceNames: Option[List[String]] = None,
    nonResourceURLs: Option[List[String]] = None
)

object PolicyRule {
  implicit val `io.k8s.api.rbac.v1beta1.PolicyRule-Decoder`
      : Decoder[PolicyRule] = deriveDecoder
  implicit val `io.k8s.api.rbac.v1beta1.PolicyRule-Encoder`
      : Encoder[PolicyRule] = deriveEncoder
}
