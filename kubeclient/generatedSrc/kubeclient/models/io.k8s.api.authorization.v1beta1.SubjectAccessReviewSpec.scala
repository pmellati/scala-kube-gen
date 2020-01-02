package kubeclient.io.k8s.api.authorization.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SubjectAccessReviewSpec(
    resourceAttributes: Option[ResourceAttributes] = None,
    nonResourceAttributes: Option[NonResourceAttributes] = None,
    uid: Option[String] = None,
    extra: Option[Map[String, List[String]]] = None,
    group: Option[List[String]] = None,
    user: Option[String] = None
)

object SubjectAccessReviewSpec {
  implicit val `io.k8s.api.authorization.v1beta1.SubjectAccessReviewSpec-Decoder`
      : Decoder[SubjectAccessReviewSpec] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1beta1.SubjectAccessReviewSpec-Encoder`
      : Encoder[SubjectAccessReviewSpec] = deriveEncoder
}
