package kubeclient.io.k8s.api.autoscaling.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HorizontalPodAutoscalerStatus(
    desiredReplicas: Int,
    lastScaleTime: Option[Time] = None,
    observedGeneration: Option[Long] = None,
    currentReplicas: Int,
    currentCPUUtilizationPercentage: Option[Int] = None
)

object HorizontalPodAutoscalerStatus {
  implicit val `io.k8s.api.autoscaling.v1.HorizontalPodAutoscalerStatus-Decoder`
      : Decoder[HorizontalPodAutoscalerStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v1.HorizontalPodAutoscalerStatus-Encoder`
      : Encoder[HorizontalPodAutoscalerStatus] = deriveEncoder
}
