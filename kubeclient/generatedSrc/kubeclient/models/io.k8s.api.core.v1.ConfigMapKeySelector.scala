package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ConfigMapKeySelector(
    key: String,
    name: Option[String] = None,
    optional: Option[Boolean] = None
)

object ConfigMapKeySelector {
  implicit val `io.k8s.api.core.v1.ConfigMapKeySelector-Decoder`
      : Decoder[ConfigMapKeySelector] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ConfigMapKeySelector-Encoder`
      : Encoder[ConfigMapKeySelector] = deriveEncoder
}
