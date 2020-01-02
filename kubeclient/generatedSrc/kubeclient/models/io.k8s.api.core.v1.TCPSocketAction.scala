package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.util.intstr.IntOrString

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TCPSocketAction(
    host: Option[String] = None,
    port: IntOrString
)

object TCPSocketAction {
  implicit val `io.k8s.api.core.v1.TCPSocketAction-Decoder`
      : Decoder[TCPSocketAction] = deriveDecoder
  implicit val `io.k8s.api.core.v1.TCPSocketAction-Encoder`
      : Encoder[TCPSocketAction] = deriveEncoder
}
