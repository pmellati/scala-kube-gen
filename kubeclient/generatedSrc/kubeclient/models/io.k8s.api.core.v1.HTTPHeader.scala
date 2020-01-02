package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HTTPHeader(
    name: String,
    value: String
)

object HTTPHeader {
  implicit val `io.k8s.api.core.v1.HTTPHeader-Decoder`: Decoder[HTTPHeader] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.HTTPHeader-Encoder`: Encoder[HTTPHeader] =
    deriveEncoder
}
