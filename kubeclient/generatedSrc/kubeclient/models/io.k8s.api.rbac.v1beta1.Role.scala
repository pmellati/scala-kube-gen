package kubeclient.io.k8s.api.rbac.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Role(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    rules: Option[List[PolicyRule]] = None
)

object Role {
  implicit val `io.k8s.api.rbac.v1beta1.Role-Decoder`: Decoder[Role] =
    deriveDecoder
  implicit val `io.k8s.api.rbac.v1beta1.Role-Encoder`: Encoder[Role] =
    deriveEncoder
}
