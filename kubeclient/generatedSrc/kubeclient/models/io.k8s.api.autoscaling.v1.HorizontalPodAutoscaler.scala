package kubeclient.io.k8s.api.autoscaling.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HorizontalPodAutoscaler(
    status: Option[HorizontalPodAutoscalerStatus] = None,
    spec: Option[HorizontalPodAutoscalerSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object HorizontalPodAutoscaler {
  implicit val `io.k8s.api.autoscaling.v1.HorizontalPodAutoscaler-Decoder`
      : Decoder[HorizontalPodAutoscaler] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v1.HorizontalPodAutoscaler-Encoder`
      : Encoder[HorizontalPodAutoscaler] = deriveEncoder
}
