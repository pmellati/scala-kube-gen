package kubeclient.io.k8s.api.autoscaling.v2beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class MetricSpec(
    `object`: Option[ObjectMetricSource] = None,
    external: Option[ExternalMetricSource] = None,
    `type`: String,
    resource: Option[ResourceMetricSource] = None,
    pods: Option[PodsMetricSource] = None
)

object MetricSpec {
  implicit val `io.k8s.api.autoscaling.v2beta1.MetricSpec-Decoder`
      : Decoder[MetricSpec] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.MetricSpec-Encoder`
      : Encoder[MetricSpec] = deriveEncoder
}
