package kubeclient.io.k8s.api.rbac.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AggregationRule(
    clusterRoleSelectors: Option[List[LabelSelector]] = None
)

object AggregationRule {
  implicit val `io.k8s.api.rbac.v1beta1.AggregationRule-Decoder`
      : Decoder[AggregationRule] = deriveDecoder
  implicit val `io.k8s.api.rbac.v1beta1.AggregationRule-Encoder`
      : Encoder[AggregationRule] = deriveEncoder
}
