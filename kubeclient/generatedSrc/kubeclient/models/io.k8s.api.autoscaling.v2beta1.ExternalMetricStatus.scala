package kubeclient.io.k8s.api.autoscaling.v2beta1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ExternalMetricStatus(
    currentAverageValue: Option[Quantity] = None,
    currentValue: Quantity,
    metricName: String,
    metricSelector: Option[LabelSelector] = None
)

object ExternalMetricStatus {
  implicit val `io.k8s.api.autoscaling.v2beta1.ExternalMetricStatus-Decoder`
      : Decoder[ExternalMetricStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.ExternalMetricStatus-Encoder`
      : Encoder[ExternalMetricStatus] = deriveEncoder
}
