package kubeclient.io.k8s.api.autoscaling.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ScaleStatus(
    replicas: Int,
    selector: Option[String] = None
)

object ScaleStatus {
  implicit val `io.k8s.api.autoscaling.v1.ScaleStatus-Decoder`
      : Decoder[ScaleStatus] = deriveDecoder
  implicit val `io.k8s.api.autoscaling.v1.ScaleStatus-Encoder`
      : Encoder[ScaleStatus] = deriveEncoder
}
