package kubeclient.io.k8s.api.authorization.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SelfSubjectRulesReview(
    status: Option[SubjectRulesReviewStatus] = None,
    spec: SelfSubjectRulesReviewSpec,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object SelfSubjectRulesReview {
  implicit val `io.k8s.api.authorization.v1beta1.SelfSubjectRulesReview-Decoder`
      : Decoder[SelfSubjectRulesReview] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1beta1.SelfSubjectRulesReview-Encoder`
      : Encoder[SelfSubjectRulesReview] = deriveEncoder
}
