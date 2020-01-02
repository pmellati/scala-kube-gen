package kubeclient.io.k8s.api.authorization.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SubjectAccessReview(
    status: Option[SubjectAccessReviewStatus] = None,
    spec: SubjectAccessReviewSpec,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object SubjectAccessReview {
  implicit val `io.k8s.api.authorization.v1.SubjectAccessReview-Decoder`
      : Decoder[SubjectAccessReview] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1.SubjectAccessReview-Encoder`
      : Encoder[SubjectAccessReview] = deriveEncoder
}
