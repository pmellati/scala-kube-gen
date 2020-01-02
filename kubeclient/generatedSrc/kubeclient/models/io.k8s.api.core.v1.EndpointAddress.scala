package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EndpointAddress(
    hostname: Option[String] = None,
    ip: String,
    nodeName: Option[String] = None,
    targetRef: Option[ObjectReference] = None
)

object EndpointAddress {
  implicit val `io.k8s.api.core.v1.EndpointAddress-Decoder`
      : Decoder[EndpointAddress] = deriveDecoder
  implicit val `io.k8s.api.core.v1.EndpointAddress-Encoder`
      : Encoder[EndpointAddress] = deriveEncoder
}
