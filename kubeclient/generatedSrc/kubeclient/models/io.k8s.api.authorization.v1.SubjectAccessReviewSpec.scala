package kubeclient.io.k8s.api.authorization.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SubjectAccessReviewSpec(
    groups: Option[List[String]] = None,
    resourceAttributes: Option[ResourceAttributes] = None,
    user: Option[String] = None,
    nonResourceAttributes: Option[NonResourceAttributes] = None,
    uid: Option[String] = None,
    extra: Option[Map[String, List[String]]] = None
)

object SubjectAccessReviewSpec {
  implicit val `io.k8s.api.authorization.v1.SubjectAccessReviewSpec-Decoder`
      : Decoder[SubjectAccessReviewSpec] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1.SubjectAccessReviewSpec-Encoder`
      : Encoder[SubjectAccessReviewSpec] = deriveEncoder
}
