package kubeclient.io.k8s.api.authorization.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SelfSubjectAccessReview(
    status: Option[SubjectAccessReviewStatus] = None,
    spec: SelfSubjectAccessReviewSpec,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object SelfSubjectAccessReview {
  implicit val `io.k8s.api.authorization.v1beta1.SelfSubjectAccessReview-Decoder`
      : Decoder[SelfSubjectAccessReview] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1beta1.SelfSubjectAccessReview-Encoder`
      : Encoder[SelfSubjectAccessReview] = deriveEncoder
}
