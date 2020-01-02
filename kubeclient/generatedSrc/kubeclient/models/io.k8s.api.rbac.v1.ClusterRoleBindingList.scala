package kubeclient.io.k8s.api.rbac.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ClusterRoleBindingList(
    apiVersion: Option[String] = None,
    items: List[ClusterRoleBinding],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ClusterRoleBindingList {
  implicit val `io.k8s.api.rbac.v1.ClusterRoleBindingList-Decoder`
      : Decoder[ClusterRoleBindingList] = deriveDecoder
  implicit val `io.k8s.api.rbac.v1.ClusterRoleBindingList-Encoder`
      : Encoder[ClusterRoleBindingList] = deriveEncoder
}
