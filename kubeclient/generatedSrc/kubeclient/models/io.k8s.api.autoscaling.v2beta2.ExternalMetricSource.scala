package kubeclient.io.k8s.api.autoscaling.v2beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ExternalMetricSource(
    metric: MetricIdentifier,
    target: MetricTarget
)

object ExternalMetricSource {
  implicit val `io.k8s.api.autoscaling.v2beta2.ExternalMetricSource-Decoder`
      : Decoder[ExternalMetricSource] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.ExternalMetricSource-Encoder`
      : Encoder[ExternalMetricSource] = deriveEncoder
}
