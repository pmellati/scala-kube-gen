package kubeclient.io.k8s.api.autoscaling.v2beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceMetricStatus(
    current: MetricValueStatus,
    name: String
)

object ResourceMetricStatus {
  implicit val `io.k8s.api.autoscaling.v2beta2.ResourceMetricStatus-Decoder`
      : Decoder[ResourceMetricStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.ResourceMetricStatus-Encoder`
      : Encoder[ResourceMetricStatus] = deriveEncoder
}
