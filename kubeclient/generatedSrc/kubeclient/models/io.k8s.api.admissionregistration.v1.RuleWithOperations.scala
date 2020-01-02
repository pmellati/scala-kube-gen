package kubeclient.io.k8s.api.admissionregistration.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RuleWithOperations(
    scope: Option[String] = None,
    apiGroups: Option[List[String]] = None,
    resources: Option[List[String]] = None,
    operations: Option[List[String]] = None,
    apiVersions: Option[List[String]] = None
)

object RuleWithOperations {
  implicit val `io.k8s.api.admissionregistration.v1.RuleWithOperations-Decoder`
      : Decoder[RuleWithOperations] = deriveDecoder
  implicit val `io.k8s.api.admissionregistration.v1.RuleWithOperations-Encoder`
      : Encoder[RuleWithOperations] = deriveEncoder
}
