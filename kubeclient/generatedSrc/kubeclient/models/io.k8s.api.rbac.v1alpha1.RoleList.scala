package kubeclient.io.k8s.api.rbac.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RoleList(
    apiVersion: Option[String] = None,
    items: List[Role],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object RoleList {
  implicit val `io.k8s.api.rbac.v1alpha1.RoleList-Decoder`: Decoder[RoleList] =
    deriveDecoder
  implicit val `io.k8s.api.rbac.v1alpha1.RoleList-Encoder`: Encoder[RoleList] =
    deriveEncoder
}
