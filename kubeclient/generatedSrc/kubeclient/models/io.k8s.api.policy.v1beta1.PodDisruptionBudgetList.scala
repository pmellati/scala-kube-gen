package kubeclient.io.k8s.api.policy.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodDisruptionBudgetList(
    apiVersion: Option[String] = None,
    items: List[PodDisruptionBudget],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object PodDisruptionBudgetList {
  implicit val `io.k8s.api.policy.v1beta1.PodDisruptionBudgetList-Decoder`
      : Decoder[PodDisruptionBudgetList] = deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.PodDisruptionBudgetList-Encoder`
      : Encoder[PodDisruptionBudgetList] = deriveEncoder
}
