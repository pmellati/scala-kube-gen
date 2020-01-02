package kubeclient.io.k8s.api.rbac.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ClusterRoleList(
    apiVersion: Option[String] = None,
    items: List[ClusterRole],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ClusterRoleList {
  implicit val `io.k8s.api.rbac.v1alpha1.ClusterRoleList-Decoder`
      : Decoder[ClusterRoleList] = deriveDecoder
  implicit val `io.k8s.api.rbac.v1alpha1.ClusterRoleList-Encoder`
      : Encoder[ClusterRoleList] = deriveEncoder
}
