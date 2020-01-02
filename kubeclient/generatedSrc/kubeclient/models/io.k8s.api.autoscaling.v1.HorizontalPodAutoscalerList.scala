package kubeclient.io.k8s.api.autoscaling.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HorizontalPodAutoscalerList(
    apiVersion: Option[String] = None,
    items: List[HorizontalPodAutoscaler],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object HorizontalPodAutoscalerList {
  implicit val `io.k8s.api.autoscaling.v1.HorizontalPodAutoscalerList-Decoder`
      : Decoder[HorizontalPodAutoscalerList] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v1.HorizontalPodAutoscalerList-Encoder`
      : Encoder[HorizontalPodAutoscalerList] = deriveEncoder
}
