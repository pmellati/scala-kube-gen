package kubeclient.io.k8s.api.autoscaling.v2beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodsMetricStatus(
    current: MetricValueStatus,
    metric: MetricIdentifier
)

object PodsMetricStatus {
  implicit val `io.k8s.api.autoscaling.v2beta2.PodsMetricStatus-Decoder`
      : Decoder[PodsMetricStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.PodsMetricStatus-Encoder`
      : Encoder[PodsMetricStatus] = deriveEncoder
}
