package kubeclient.io.k8s.api.autoscaling.v2beta2

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class MetricIdentifier(
    name: String,
    selector: Option[LabelSelector] = None
)

object MetricIdentifier {
  implicit val `io.k8s.api.autoscaling.v2beta2.MetricIdentifier-Decoder`
      : Decoder[MetricIdentifier] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v2beta2.MetricIdentifier-Encoder`
      : Encoder[MetricIdentifier] = deriveEncoder
}
