package kubeclient.io.k8s.api.autoscaling.v2beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ExternalMetricStatus(
    current: MetricValueStatus,
    metric: MetricIdentifier
)

object ExternalMetricStatus {
  implicit val `io.k8s.api.autoscaling.v2beta2.ExternalMetricStatus-Decoder`
      : Decoder[ExternalMetricStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.ExternalMetricStatus-Encoder`
      : Encoder[ExternalMetricStatus] = deriveEncoder
}
