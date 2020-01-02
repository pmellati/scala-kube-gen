package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Toleration(
    effect: Option[String] = None,
    key: Option[String] = None,
    tolerationSeconds: Option[Long] = None,
    operator: Option[String] = None,
    value: Option[String] = None
)

object Toleration {
  implicit val `io.k8s.api.core.v1.Toleration-Decoder`: Decoder[Toleration] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Toleration-Encoder`: Encoder[Toleration] =
    deriveEncoder
}
