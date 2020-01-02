package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EnvFromSource(
    configMapRef: Option[ConfigMapEnvSource] = None,
    prefix: Option[String] = None,
    secretRef: Option[SecretEnvSource] = None
)

object EnvFromSource {
  implicit val `io.k8s.api.core.v1.EnvFromSource-Decoder`
      : Decoder[EnvFromSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.EnvFromSource-Encoder`
      : Encoder[EnvFromSource] = deriveEncoder
}
