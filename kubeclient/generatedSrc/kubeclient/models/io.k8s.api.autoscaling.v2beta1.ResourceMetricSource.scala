package kubeclient.io.k8s.api.autoscaling.v2beta1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceMetricSource(
    name: String,
    targetAverageUtilization: Option[Int] = None,
    targetAverageValue: Option[Quantity] = None
)

object ResourceMetricSource {
  implicit val `io.k8s.api.autoscaling.v2beta1.ResourceMetricSource-Decoder`
      : Decoder[ResourceMetricSource] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.ResourceMetricSource-Encoder`
      : Encoder[ResourceMetricSource] = deriveEncoder
}
