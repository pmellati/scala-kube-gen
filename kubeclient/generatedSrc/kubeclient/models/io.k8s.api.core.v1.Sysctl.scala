package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Sysctl(
    name: String,
    value: String
)

object Sysctl {
  implicit val `io.k8s.api.core.v1.Sysctl-Decoder`: Decoder[Sysctl] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Sysctl-Encoder`: Encoder[Sysctl] =
    deriveEncoder
}
