package kubeclient.io.k8s.api.authorization.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SelfSubjectAccessReviewSpec(
    nonResourceAttributes: Option[NonResourceAttributes] = None,
    resourceAttributes: Option[ResourceAttributes] = None
)

object SelfSubjectAccessReviewSpec {
  implicit val `io.k8s.api.authorization.v1.SelfSubjectAccessReviewSpec-Decoder`
      : Decoder[SelfSubjectAccessReviewSpec] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1.SelfSubjectAccessReviewSpec-Encoder`
      : Encoder[SelfSubjectAccessReviewSpec] = deriveEncoder
}
