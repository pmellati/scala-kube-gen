package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ClientIPConfig(
    timeoutSeconds: Option[Int] = None
)

object ClientIPConfig {
  implicit val `io.k8s.api.core.v1.ClientIPConfig-Decoder`
      : Decoder[ClientIPConfig] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ClientIPConfig-Encoder`
      : Encoder[ClientIPConfig] = deriveEncoder
}
