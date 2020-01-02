package kubeclient.io.k8s.api.rbac.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ClusterRole(
    aggregationRule: Option[AggregationRule] = None,
    metadata: Option[ObjectMeta] = None,
    rules: Option[List[PolicyRule]] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object ClusterRole {
  implicit val `io.k8s.api.rbac.v1beta1.ClusterRole-Decoder`
      : Decoder[ClusterRole] = deriveDecoder
  implicit val `io.k8s.api.rbac.v1beta1.ClusterRole-Encoder`
      : Encoder[ClusterRole] = deriveEncoder
}
