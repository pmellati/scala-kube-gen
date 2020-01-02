package kubeclient.io.k8s.api.authorization.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LocalSubjectAccessReview(
    status: Option[SubjectAccessReviewStatus] = None,
    spec: SubjectAccessReviewSpec,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object LocalSubjectAccessReview {
  implicit val `io.k8s.api.authorization.v1.LocalSubjectAccessReview-Decoder`
      : Decoder[LocalSubjectAccessReview] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1.LocalSubjectAccessReview-Encoder`
      : Encoder[LocalSubjectAccessReview] = deriveEncoder
}
