package kubeclient.io.k8s.api.policy.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodDisruptionBudget(
    status: Option[PodDisruptionBudgetStatus] = None,
    spec: Option[PodDisruptionBudgetSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object PodDisruptionBudget {
  implicit val `io.k8s.api.policy.v1beta1.PodDisruptionBudget-Decoder`
      : Decoder[PodDisruptionBudget] = deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.PodDisruptionBudget-Encoder`
      : Encoder[PodDisruptionBudget] = deriveEncoder
}
