package kubeclient.io.k8s.api.authentication.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TokenReview(
    status: Option[TokenReviewStatus] = None,
    spec: TokenReviewSpec,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object TokenReview {
  implicit val `io.k8s.api.authentication.v1beta1.TokenReview-Decoder`
      : Decoder[TokenReview] = deriveDecoder
  implicit val `io.k8s.api.authentication.v1beta1.TokenReview-Encoder`
      : Encoder[TokenReview] = deriveEncoder
}
