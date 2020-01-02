package kubeclient.io.k8s.api.autoscaling.v2beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ObjectMetricStatus(
    current: MetricValueStatus,
    describedObject: CrossVersionObjectReference,
    metric: MetricIdentifier
)

object ObjectMetricStatus {
  implicit val `io.k8s.api.autoscaling.v2beta2.ObjectMetricStatus-Decoder`
      : Decoder[ObjectMetricStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.ObjectMetricStatus-Encoder`
      : Encoder[ObjectMetricStatus] = deriveEncoder
}
