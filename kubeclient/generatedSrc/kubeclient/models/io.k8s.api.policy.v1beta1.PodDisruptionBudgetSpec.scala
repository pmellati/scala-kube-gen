package kubeclient.io.k8s.api.policy.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector
import kubeclient.io.k8s.apimachinery.pkg.util.intstr.IntOrString

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodDisruptionBudgetSpec(
    maxUnavailable: Option[IntOrString] = None,
    minAvailable: Option[IntOrString] = None,
    selector: Option[LabelSelector] = None
)

object PodDisruptionBudgetSpec {
  implicit val `io.k8s.api.policy.v1beta1.PodDisruptionBudgetSpec-Decoder`
      : Decoder[PodDisruptionBudgetSpec] = deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.PodDisruptionBudgetSpec-Encoder`
      : Encoder[PodDisruptionBudgetSpec] = deriveEncoder
}
