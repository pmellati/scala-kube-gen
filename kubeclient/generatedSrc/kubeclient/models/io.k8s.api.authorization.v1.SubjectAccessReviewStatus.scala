package kubeclient.io.k8s.api.authorization.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SubjectAccessReviewStatus(
    allowed: Boolean,
    denied: Option[Boolean] = None,
    evaluationError: Option[String] = None,
    reason: Option[String] = None
)

object SubjectAccessReviewStatus {
  implicit val `io.k8s.api.authorization.v1.SubjectAccessReviewStatus-Decoder`
      : Decoder[SubjectAccessReviewStatus] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1.SubjectAccessReviewStatus-Encoder`
      : Encoder[SubjectAccessReviewStatus] = deriveEncoder
}
