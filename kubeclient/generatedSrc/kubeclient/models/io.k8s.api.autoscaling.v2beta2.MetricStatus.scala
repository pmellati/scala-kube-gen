package kubeclient.io.k8s.api.autoscaling.v2beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class MetricStatus(
    `object`: Option[ObjectMetricStatus] = None,
    external: Option[ExternalMetricStatus] = None,
    `type`: String,
    resource: Option[ResourceMetricStatus] = None,
    pods: Option[PodsMetricStatus] = None
)

object MetricStatus {
  implicit val `io.k8s.api.autoscaling.v2beta2.MetricStatus-Decoder`
      : Decoder[MetricStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.MetricStatus-Encoder`
      : Encoder[MetricStatus] = deriveEncoder
}
