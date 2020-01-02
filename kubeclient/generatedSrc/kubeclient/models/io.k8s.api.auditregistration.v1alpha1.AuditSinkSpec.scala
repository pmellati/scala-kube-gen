package kubeclient.io.k8s.api.auditregistration.v1alpha1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AuditSinkSpec(
    policy: Policy,
    webhook: Webhook
)

object AuditSinkSpec {
  implicit val `io.k8s.api.auditregistration.v1alpha1.AuditSinkSpec-Decoder`
      : Decoder[AuditSinkSpec] = deriveDecoder
  implicit val `io.k8s.api.auditregistration.v1alpha1.AuditSinkSpec-Encoder`
      : Encoder[AuditSinkSpec] = deriveEncoder
}
