package kubeclient.io.k8s.api.autoscaling.v2beta1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodsMetricStatus(
    currentAverageValue: Quantity,
    metricName: String,
    selector: Option[LabelSelector] = None
)

object PodsMetricStatus {
  implicit val `io.k8s.api.autoscaling.v2beta1.PodsMetricStatus-Decoder`
      : Decoder[PodsMetricStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.PodsMetricStatus-Encoder`
      : Encoder[PodsMetricStatus] = deriveEncoder
}
