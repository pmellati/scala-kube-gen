package kubeclient.io.k8s.api.authorization.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SubjectRulesReviewStatus(
    evaluationError: Option[String] = None,
    incomplete: Boolean,
    nonResourceRules: List[NonResourceRule],
    resourceRules: List[ResourceRule]
)

object SubjectRulesReviewStatus {
  implicit val `io.k8s.api.authorization.v1beta1.SubjectRulesReviewStatus-Decoder`
      : Decoder[SubjectRulesReviewStatus] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1beta1.SubjectRulesReviewStatus-Encoder`
      : Encoder[SubjectRulesReviewStatus] = deriveEncoder
}
