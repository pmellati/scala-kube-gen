package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ScaleStatus(
    replicas: Int,
    selector: Option[Map[String, String]] = None,
    targetSelector: Option[String] = None
)

object ScaleStatus {
  implicit val `io.k8s.api.extensions.v1beta1.ScaleStatus-Decoder`
      : Decoder[ScaleStatus] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.ScaleStatus-Encoder`
      : Encoder[ScaleStatus] = deriveEncoder
}
