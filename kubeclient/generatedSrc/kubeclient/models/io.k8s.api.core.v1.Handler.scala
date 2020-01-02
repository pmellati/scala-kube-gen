package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Handler(
    exec: Option[ExecAction] = None,
    httpGet: Option[HTTPGetAction] = None,
    tcpSocket: Option[TCPSocketAction] = None
)

object Handler {
  implicit val `io.k8s.api.core.v1.Handler-Decoder`: Decoder[Handler] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Handler-Encoder`: Encoder[Handler] =
    deriveEncoder
}
