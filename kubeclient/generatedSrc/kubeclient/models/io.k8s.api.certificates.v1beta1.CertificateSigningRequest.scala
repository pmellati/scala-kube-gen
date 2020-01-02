package kubeclient.io.k8s.api.certificates.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CertificateSigningRequest(
    status: Option[CertificateSigningRequestStatus] = None,
    spec: Option[CertificateSigningRequestSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object CertificateSigningRequest {
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequest-Decoder`
      : Decoder[CertificateSigningRequest] = deriveDecoder
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequest-Encoder`
      : Encoder[CertificateSigningRequest] = deriveEncoder
}
