package kubeclient.io.k8s.api.autoscaling.v2beta2

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HorizontalPodAutoscalerCondition(
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object HorizontalPodAutoscalerCondition {
  implicit val `io.k8s.api.autoscaling.v2beta2.HorizontalPodAutoscalerCondition-Decoder`
      : Decoder[HorizontalPodAutoscalerCondition] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.HorizontalPodAutoscalerCondition-Encoder`
      : Encoder[HorizontalPodAutoscalerCondition] = deriveEncoder
}
