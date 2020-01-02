package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EndpointSubset(
    addresses: Option[List[EndpointAddress]] = None,
    notReadyAddresses: Option[List[EndpointAddress]] = None,
    ports: Option[List[EndpointPort]] = None
)

object EndpointSubset {
  implicit val `io.k8s.api.core.v1.EndpointSubset-Decoder`
      : Decoder[EndpointSubset] = deriveDecoder
  implicit val `io.k8s.api.core.v1.EndpointSubset-Encoder`
      : Encoder[EndpointSubset] = deriveEncoder
}
