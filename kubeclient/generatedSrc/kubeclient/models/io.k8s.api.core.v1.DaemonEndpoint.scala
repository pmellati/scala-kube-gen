package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DaemonEndpoint(
    Port: Int
)

object DaemonEndpoint {
  implicit val `io.k8s.api.core.v1.DaemonEndpoint-Decoder`
      : Decoder[DaemonEndpoint] = deriveDecoder
  implicit val `io.k8s.api.core.v1.DaemonEndpoint-Encoder`
      : Encoder[DaemonEndpoint] = deriveEncoder
}
