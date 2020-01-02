package kubeclient.io.k8s.api.autoscaling.v2beta2

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class MetricValueStatus(
    averageUtilization: Option[Int] = None,
    averageValue: Option[Quantity] = None,
    value: Option[Quantity] = None
)

object MetricValueStatus {
  implicit val `io.k8s.api.autoscaling.v2beta2.MetricValueStatus-Decoder`
      : Decoder[MetricValueStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.MetricValueStatus-Encoder`
      : Encoder[MetricValueStatus] = deriveEncoder
}
