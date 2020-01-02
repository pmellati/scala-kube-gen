package kubeclient.io.k8s.api.autoscaling.v2beta1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ExternalMetricSource(
    metricName: String,
    metricSelector: Option[LabelSelector] = None,
    targetAverageValue: Option[Quantity] = None,
    targetValue: Option[Quantity] = None
)

object ExternalMetricSource {
  implicit val `io.k8s.api.autoscaling.v2beta1.ExternalMetricSource-Decoder`
      : Decoder[ExternalMetricSource] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.ExternalMetricSource-Encoder`
      : Encoder[ExternalMetricSource] = deriveEncoder
}
