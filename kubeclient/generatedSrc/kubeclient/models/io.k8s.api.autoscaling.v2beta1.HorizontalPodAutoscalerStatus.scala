package kubeclient.io.k8s.api.autoscaling.v2beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HorizontalPodAutoscalerStatus(
    conditions: List[HorizontalPodAutoscalerCondition],
    desiredReplicas: Int,
    currentMetrics: Option[List[MetricStatus]] = None,
    lastScaleTime: Option[Time] = None,
    observedGeneration: Option[Long] = None,
    currentReplicas: Int
)

object HorizontalPodAutoscalerStatus {
  implicit val `io.k8s.api.autoscaling.v2beta1.HorizontalPodAutoscalerStatus-Decoder`
      : Decoder[HorizontalPodAutoscalerStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.HorizontalPodAutoscalerStatus-Encoder`
      : Encoder[HorizontalPodAutoscalerStatus] = deriveEncoder
}
