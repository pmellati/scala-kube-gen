package kubeclient.io.k8s.api.authentication.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TokenReviewStatus(
    audiences: Option[List[String]] = None,
    authenticated: Option[Boolean] = None,
    error: Option[String] = None,
    user: Option[UserInfo] = None
)

object TokenReviewStatus {
  implicit val `io.k8s.api.authentication.v1beta1.TokenReviewStatus-Decoder`
      : Decoder[TokenReviewStatus] = deriveDecoder
  implicit val `io.k8s.api.authentication.v1beta1.TokenReviewStatus-Encoder`
      : Encoder[TokenReviewStatus] = deriveEncoder
}
