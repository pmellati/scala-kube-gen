package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class KeyToPath(
    key: String,
    mode: Option[Int] = None,
    path: String
)

object KeyToPath {
  implicit val `io.k8s.api.core.v1.KeyToPath-Decoder`: Decoder[KeyToPath] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.KeyToPath-Encoder`: Encoder[KeyToPath] =
    deriveEncoder
}
