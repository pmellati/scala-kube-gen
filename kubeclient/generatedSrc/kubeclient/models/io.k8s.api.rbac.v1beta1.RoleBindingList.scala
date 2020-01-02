package kubeclient.io.k8s.api.rbac.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RoleBindingList(
    apiVersion: Option[String] = None,
    items: List[RoleBinding],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object RoleBindingList {
  implicit val `io.k8s.api.rbac.v1beta1.RoleBindingList-Decoder`
      : Decoder[RoleBindingList] = deriveDecoder
  implicit val `io.k8s.api.rbac.v1beta1.RoleBindingList-Encoder`
      : Encoder[RoleBindingList] = deriveEncoder
}
