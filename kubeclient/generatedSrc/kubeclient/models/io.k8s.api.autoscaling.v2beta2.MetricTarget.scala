package kubeclient.io.k8s.api.autoscaling.v2beta2

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class MetricTarget(
    averageUtilization: Option[Int] = None,
    averageValue: Option[Quantity] = None,
    `type`: String,
    value: Option[Quantity] = None
)

object MetricTarget {
  implicit val `io.k8s.api.autoscaling.v2beta2.MetricTarget-Decoder`
      : Decoder[MetricTarget] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.MetricTarget-Encoder`
      : Encoder[MetricTarget] = deriveEncoder
}
