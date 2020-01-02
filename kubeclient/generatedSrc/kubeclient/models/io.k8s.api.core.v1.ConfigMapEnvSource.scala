package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ConfigMapEnvSource(
    name: Option[String] = None,
    optional: Option[Boolean] = None
)

object ConfigMapEnvSource {
  implicit val `io.k8s.api.core.v1.ConfigMapEnvSource-Decoder`
      : Decoder[ConfigMapEnvSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ConfigMapEnvSource-Encoder`
      : Encoder[ConfigMapEnvSource] = deriveEncoder
}
