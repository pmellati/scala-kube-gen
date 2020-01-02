package kubeclient.io.k8s.api.apps.v1beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ScaleSpec(
    replicas: Option[Int] = None
)

object ScaleSpec {
  implicit val `io.k8s.api.apps.v1beta2.ScaleSpec-Decoder`: Decoder[ScaleSpec] =
    deriveDecoder
  implicit val `io.k8s.api.apps.v1beta2.ScaleSpec-Encoder`: Encoder[ScaleSpec] =
    deriveEncoder
}
