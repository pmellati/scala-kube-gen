package kubeclient.io.k8s.api.rbac.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RoleBinding(
    subjects: Option[List[Subject]] = None,
    metadata: Option[ObjectMeta] = None,
    roleRef: RoleRef,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object RoleBinding {
  implicit val `io.k8s.api.rbac.v1beta1.RoleBinding-Decoder`
      : Decoder[RoleBinding] = deriveDecoder
  implicit val `io.k8s.api.rbac.v1beta1.RoleBinding-Encoder`
      : Encoder[RoleBinding] = deriveEncoder
}
