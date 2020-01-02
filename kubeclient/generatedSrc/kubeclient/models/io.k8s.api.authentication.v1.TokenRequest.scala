package kubeclient.io.k8s.api.authentication.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TokenRequest(
    status: Option[TokenRequestStatus] = None,
    spec: TokenRequestSpec,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object TokenRequest {
  implicit val `io.k8s.api.authentication.v1.TokenRequest-Decoder`
      : Decoder[TokenRequest] = deriveDecoder
  implicit val `io.k8s.api.authentication.v1.TokenRequest-Encoder`
      : Encoder[TokenRequest] = deriveEncoder
}
