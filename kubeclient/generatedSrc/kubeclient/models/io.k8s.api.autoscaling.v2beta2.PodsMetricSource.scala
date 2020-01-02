package kubeclient.io.k8s.api.autoscaling.v2beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodsMetricSource(
    metric: MetricIdentifier,
    target: MetricTarget
)

object PodsMetricSource {
  implicit val `io.k8s.api.autoscaling.v2beta2.PodsMetricSource-Decoder`
      : Decoder[PodsMetricSource] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.PodsMetricSource-Encoder`
      : Encoder[PodsMetricSource] = deriveEncoder
}
