package kubeclient.io.k8s.api.autoscaling.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HorizontalPodAutoscalerSpec(
    maxReplicas: Int,
    minReplicas: Option[Int] = None,
    scaleTargetRef: CrossVersionObjectReference,
    targetCPUUtilizationPercentage: Option[Int] = None
)

object HorizontalPodAutoscalerSpec {
  implicit val `io.k8s.api.autoscaling.v1.HorizontalPodAutoscalerSpec-Decoder`
      : Decoder[HorizontalPodAutoscalerSpec] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v1.HorizontalPodAutoscalerSpec-Encoder`
      : Encoder[HorizontalPodAutoscalerSpec] = deriveEncoder
}
