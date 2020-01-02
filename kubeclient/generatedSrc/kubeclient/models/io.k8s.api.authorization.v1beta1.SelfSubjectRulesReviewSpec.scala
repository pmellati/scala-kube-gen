package kubeclient.io.k8s.api.authorization.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SelfSubjectRulesReviewSpec(
    namespace: Option[String] = None
)

object SelfSubjectRulesReviewSpec {
  implicit val `io.k8s.api.authorization.v1beta1.SelfSubjectRulesReviewSpec-Decoder`
      : Decoder[SelfSubjectRulesReviewSpec] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1beta1.SelfSubjectRulesReviewSpec-Encoder`
      : Encoder[SelfSubjectRulesReviewSpec] = deriveEncoder
}
