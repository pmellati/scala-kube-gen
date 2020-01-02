package kubeclient.io.k8s.api.discovery.v1alpha1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EndpointPort(
    name: Option[String] = None,
    port: Option[Int] = None,
    protocol: Option[String] = None
)

object EndpointPort {
  implicit val `io.k8s.api.discovery.v1alpha1.EndpointPort-Decoder`
      : Decoder[EndpointPort] = deriveDecoder
  implicit val `io.k8s.api.discovery.v1alpha1.EndpointPort-Encoder`
      : Encoder[EndpointPort] = deriveEncoder
}
