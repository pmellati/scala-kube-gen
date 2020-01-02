package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SessionAffinityConfig(
    clientIP: Option[ClientIPConfig] = None
)

object SessionAffinityConfig {
  implicit val `io.k8s.api.core.v1.SessionAffinityConfig-Decoder`
      : Decoder[SessionAffinityConfig] = deriveDecoder
  implicit val `io.k8s.api.core.v1.SessionAffinityConfig-Encoder`
      : Encoder[SessionAffinityConfig] = deriveEncoder
}
