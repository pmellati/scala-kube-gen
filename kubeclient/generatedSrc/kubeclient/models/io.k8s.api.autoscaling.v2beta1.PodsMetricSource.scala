package kubeclient.io.k8s.api.autoscaling.v2beta1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodsMetricSource(
    metricName: String,
    selector: Option[LabelSelector] = None,
    targetAverageValue: Quantity
)

object PodsMetricSource {
  implicit val `io.k8s.api.autoscaling.v2beta1.PodsMetricSource-Decoder`
      : Decoder[PodsMetricSource] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta1.PodsMetricSource-Encoder`
      : Encoder[PodsMetricSource] = deriveEncoder
}
