package kubeclient.io.k8s.api.certificates.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CertificateSigningRequestSpec(
    username: Option[String] = None,
    groups: Option[List[String]] = None,
    usages: Option[List[String]] = None,
    request: String,
    uid: Option[String] = None,
    extra: Option[Map[String, List[String]]] = None
)

object CertificateSigningRequestSpec {
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequestSpec-Decoder`
      : Decoder[CertificateSigningRequestSpec] = deriveDecoder
  implicit val `io.k8s.api.certificates.v1beta1.CertificateSigningRequestSpec-Encoder`
      : Encoder[CertificateSigningRequestSpec] = deriveEncoder
}
