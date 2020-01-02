package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Probe(
    timeoutSeconds: Option[Int] = None,
    tcpSocket: Option[TCPSocketAction] = None,
    initialDelaySeconds: Option[Int] = None,
    failureThreshold: Option[Int] = None,
    httpGet: Option[HTTPGetAction] = None,
    successThreshold: Option[Int] = None,
    exec: Option[ExecAction] = None,
    periodSeconds: Option[Int] = None
)

object Probe {
  implicit val `io.k8s.api.core.v1.Probe-Decoder`: Decoder[Probe] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Probe-Encoder`: Encoder[Probe] =
    deriveEncoder
}
