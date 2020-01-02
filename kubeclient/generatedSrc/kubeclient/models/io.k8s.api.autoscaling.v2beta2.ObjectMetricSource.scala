package kubeclient.io.k8s.api.autoscaling.v2beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ObjectMetricSource(
    describedObject: CrossVersionObjectReference,
    metric: MetricIdentifier,
    target: MetricTarget
)

object ObjectMetricSource {
  implicit val `io.k8s.api.autoscaling.v2beta2.ObjectMetricSource-Decoder`
      : Decoder[ObjectMetricSource] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.ObjectMetricSource-Encoder`
      : Encoder[ObjectMetricSource] = deriveEncoder
}
