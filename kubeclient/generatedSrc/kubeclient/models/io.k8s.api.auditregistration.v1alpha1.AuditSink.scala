package kubeclient.io.k8s.api.auditregistration.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AuditSink(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    spec: Option[AuditSinkSpec] = None
)

object AuditSink {
  implicit val `io.k8s.api.auditregistration.v1alpha1.AuditSink-Decoder`
      : Decoder[AuditSink] = deriveDecoder
  implicit val `io.k8s.api.auditregistration.v1alpha1.AuditSink-Encoder`
      : Encoder[AuditSink] = deriveEncoder
}
