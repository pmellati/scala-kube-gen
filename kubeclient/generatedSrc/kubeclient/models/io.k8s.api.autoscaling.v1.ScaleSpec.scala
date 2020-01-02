package kubeclient.io.k8s.api.autoscaling.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ScaleSpec(
    replicas: Option[Int] = None
)

object ScaleSpec {
  implicit val `io.k8s.api.autoscaling.v1.ScaleSpec-Decoder`
      : Decoder[ScaleSpec] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v1.ScaleSpec-Encoder`
      : Encoder[ScaleSpec] = deriveEncoder
}
