package kubeclient.io.k8s.api.authorization.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SelfSubjectRulesReviewSpec(
    namespace: Option[String] = None
)

object SelfSubjectRulesReviewSpec {
  implicit val `io.k8s.api.authorization.v1.SelfSubjectRulesReviewSpec-Decoder`
      : Decoder[SelfSubjectRulesReviewSpec] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1.SelfSubjectRulesReviewSpec-Encoder`
      : Encoder[SelfSubjectRulesReviewSpec] = deriveEncoder
}
