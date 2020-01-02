package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SecretKeySelector(
    key: String,
    name: Option[String] = None,
    optional: Option[Boolean] = None
)

object SecretKeySelector {
  implicit val `io.k8s.api.core.v1.SecretKeySelector-Decoder`
      : Decoder[SecretKeySelector] = deriveDecoder
  implicit val `io.k8s.api.core.v1.SecretKeySelector-Encoder`
      : Encoder[SecretKeySelector] = deriveEncoder
}
