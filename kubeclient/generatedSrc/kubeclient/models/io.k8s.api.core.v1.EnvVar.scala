package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EnvVar(
    name: String,
    value: Option[String] = None,
    valueFrom: Option[EnvVarSource] = None
)

object EnvVar {
  implicit val `io.k8s.api.core.v1.EnvVar-Decoder`: Decoder[EnvVar] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.EnvVar-Encoder`: Encoder[EnvVar] =
    deriveEncoder
}
