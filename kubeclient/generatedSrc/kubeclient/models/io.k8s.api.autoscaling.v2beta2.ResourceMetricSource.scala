package kubeclient.io.k8s.api.autoscaling.v2beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceMetricSource(
    name: String,
    target: MetricTarget
)

object ResourceMetricSource {
  implicit val `io.k8s.api.autoscaling.v2beta2.ResourceMetricSource-Decoder`
      : Decoder[ResourceMetricSource] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.ResourceMetricSource-Encoder`
      : Encoder[ResourceMetricSource] = deriveEncoder
}
