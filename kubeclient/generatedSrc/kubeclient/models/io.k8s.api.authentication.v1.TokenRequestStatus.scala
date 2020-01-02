package kubeclient.io.k8s.api.authentication.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TokenRequestStatus(
    expirationTimestamp: Time,
    token: String
)

object TokenRequestStatus {
  implicit val `io.k8s.api.authentication.v1.TokenRequestStatus-Decoder`
      : Decoder[TokenRequestStatus] = deriveDecoder
  implicit val `io.k8s.api.authentication.v1.TokenRequestStatus-Encoder`
      : Encoder[TokenRequestStatus] = deriveEncoder
}
