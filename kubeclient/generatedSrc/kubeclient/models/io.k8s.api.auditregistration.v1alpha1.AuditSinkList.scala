package kubeclient.io.k8s.api.auditregistration.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AuditSinkList(
    apiVersion: Option[String] = None,
    items: List[AuditSink],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object AuditSinkList {
  implicit val `io.k8s.api.auditregistration.v1alpha1.AuditSinkList-Decoder`
      : Decoder[AuditSinkList] = deriveDecoder
  implicit val `io.k8s.api.auditregistration.v1alpha1.AuditSinkList-Encoder`
      : Encoder[AuditSinkList] = deriveEncoder
}
