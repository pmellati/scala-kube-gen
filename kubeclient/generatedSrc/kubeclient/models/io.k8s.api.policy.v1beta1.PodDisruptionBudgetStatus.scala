package kubeclient.io.k8s.api.policy.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodDisruptionBudgetStatus(
    disruptionsAllowed: Int,
    expectedPods: Int,
    observedGeneration: Option[Long] = None,
    currentHealthy: Int,
    disruptedPods: Option[Map[String, Time]] = None,
    desiredHealthy: Int
)

object PodDisruptionBudgetStatus {
  implicit val `io.k8s.api.policy.v1beta1.PodDisruptionBudgetStatus-Decoder`
      : Decoder[PodDisruptionBudgetStatus] = deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.PodDisruptionBudgetStatus-Encoder`
      : Encoder[PodDisruptionBudgetStatus] = deriveEncoder
}
