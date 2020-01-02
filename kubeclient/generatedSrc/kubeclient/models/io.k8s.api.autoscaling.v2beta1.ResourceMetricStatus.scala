package kubeclient.io.k8s.api.autoscaling.v2beta1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceMetricStatus(
    currentAverageUtilization: Option[Int] = None,
    currentAverageValue: Quantity,
    name: String
)

object ResourceMetricStatus {
  implicit val `io.k8s.api.autoscaling.v2beta1.ResourceMetricStatus-Decoder`
      : Decoder[ResourceMetricStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.ResourceMetricStatus-Encoder`
      : Encoder[ResourceMetricStatus] = deriveEncoder
}
