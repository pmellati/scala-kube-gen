package kubeclient.io.k8s.api.autoscaling.v2beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HorizontalPodAutoscalerSpec(
    maxReplicas: Int,
    metrics: Option[List[MetricSpec]] = None,
    minReplicas: Option[Int] = None,
    scaleTargetRef: CrossVersionObjectReference
)

object HorizontalPodAutoscalerSpec {
  implicit val `io.k8s.api.autoscaling.v2beta1.HorizontalPodAutoscalerSpec-Decoder`
      : Decoder[HorizontalPodAutoscalerSpec] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.HorizontalPodAutoscalerSpec-Encoder`
      : Encoder[HorizontalPodAutoscalerSpec] = deriveEncoder
}
