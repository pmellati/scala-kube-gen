package kubeclient.io.k8s.api.certificates.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CertificateSigningRequestCondition(
    lastUpdateTime: Option[Time] = None,
    message: Option[String] = None,
    reason: Option[String] = None,
    `type`: String
)

object CertificateSigningRequestCondition {
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequestCondition-Decoder`
      : Decoder[CertificateSigningRequestCondition] = deriveDecoder
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequestCondition-Encoder`
      : Encoder[CertificateSigningRequestCondition] = deriveEncoder
}
