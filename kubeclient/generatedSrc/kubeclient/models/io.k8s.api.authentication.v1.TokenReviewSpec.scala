package kubeclient.io.k8s.api.authentication.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TokenReviewSpec(
    audiences: Option[List[String]] = None,
    token: Option[String] = None
)

object TokenReviewSpec {
  implicit val `io.k8s.api.authentication.v1.TokenReviewSpec-Decoder`
      : Decoder[TokenReviewSpec] = deriveDecoder
  implicit val `io.k8s.api.authentication.v1.TokenReviewSpec-Encoder`
      : Encoder[TokenReviewSpec] = deriveEncoder
}
