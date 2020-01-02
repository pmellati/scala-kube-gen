package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ConfigMapNodeConfigSource(
    name: String,
    resourceVersion: Option[String] = None,
    kubeletConfigKey: String,
    uid: Option[String] = None,
    namespace: String
)

object ConfigMapNodeConfigSource {
  implicit val `io.k8s.api.core.v1.ConfigMapNodeConfigSource-Decoder`
      : Decoder[ConfigMapNodeConfigSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ConfigMapNodeConfigSource-Encoder`
      : Encoder[ConfigMapNodeConfigSource] = deriveEncoder
}
