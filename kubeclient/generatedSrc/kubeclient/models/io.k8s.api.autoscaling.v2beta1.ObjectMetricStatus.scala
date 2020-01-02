package kubeclient.io.k8s.api.autoscaling.v2beta1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ObjectMetricStatus(
    metricName: String,
    averageValue: Option[Quantity] = None,
    target: CrossVersionObjectReference,
    selector: Option[LabelSelector] = None,
    currentValue: Quantity
)

object ObjectMetricStatus {
  implicit val `io.k8s.api.autoscaling.v2beta1.ObjectMetricStatus-Decoder`
      : Decoder[ObjectMetricStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.ObjectMetricStatus-Encoder`
      : Encoder[ObjectMetricStatus] = deriveEncoder
}
