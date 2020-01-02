package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SecretEnvSource(
    name: Option[String] = None,
    optional: Option[Boolean] = None
)

object SecretEnvSource {
  implicit val `io.k8s.api.core.v1.SecretEnvSource-Decoder`
      : Decoder[SecretEnvSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.SecretEnvSource-Encoder`
      : Encoder[SecretEnvSource] = deriveEncoder
}
