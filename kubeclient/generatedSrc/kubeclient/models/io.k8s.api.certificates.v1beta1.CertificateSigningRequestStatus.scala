package kubeclient.io.k8s.api.certificates.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CertificateSigningRequestStatus(
    certificate: Option[String] = None,
    conditions: Option[List[CertificateSigningRequestCondition]] = None
)

object CertificateSigningRequestStatus {
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequestStatus-Decoder`
      : Decoder[CertificateSigningRequestStatus] = deriveDecoder
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequestStatus-Encoder`
      : Encoder[CertificateSigningRequestStatus] = deriveEncoder
}
