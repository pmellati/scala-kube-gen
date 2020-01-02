package kubeclient.io.k8s.api.authentication.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TokenRequestSpec(
    audiences: List[String],
    boundObjectRef: Option[BoundObjectReference] = None,
    expirationSeconds: Option[Long] = None
)

object TokenRequestSpec {
  implicit val `io.k8s.api.authentication.v1.TokenRequestSpec-Decoder`
      : Decoder[TokenRequestSpec] = deriveDecoder
  implicit val `io.k8s.api.authentication.v1.TokenRequestSpec-Encoder`
      : Encoder[TokenRequestSpec] = deriveEncoder
}
