package kubeclient.io.k8s.api.rbac.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RoleRef(
    apiGroup: String,
    kind: String,
    name: String
)

object RoleRef {
  implicit val `io.k8s.api.rbac.v1beta1.RoleRef-Decoder`: Decoder[RoleRef] =
    deriveDecoder
  implicit val `io.k8s.api.rbac.v1beta1.RoleRef-Encoder`: Encoder[RoleRef] =
    deriveEncoder
}
