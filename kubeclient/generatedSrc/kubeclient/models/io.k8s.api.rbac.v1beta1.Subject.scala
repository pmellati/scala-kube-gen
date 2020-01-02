package kubeclient.io.k8s.api.rbac.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Subject(
    apiGroup: Option[String] = None,
    kind: String,
    name: String,
    namespace: Option[String] = None
)

object Subject {
  implicit val `io.k8s.api.rbac.v1beta1.Subject-Decoder`: Decoder[Subject] =
    deriveDecoder
  implicit val `io.k8s.api.rbac.v1beta1.Subject-Encoder`: Encoder[Subject] =
    deriveEncoder
}
