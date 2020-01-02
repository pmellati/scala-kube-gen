package kubeclient.io.k8s.api.certificates.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CertificateSigningRequestList(
    apiVersion: Option[String] = None,
    items: List[CertificateSigningRequest],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object CertificateSigningRequestList {
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequestList-Decoder`
      : Decoder[CertificateSigningRequestList] = deriveDecoder
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequestList-Encoder`
      : Encoder[CertificateSigningRequestList] = deriveEncoder
}
