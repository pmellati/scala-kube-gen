package kubeclient.io.k8s.api.discovery.v1alpha1

import kubeclient.io.k8s.api.core.v1.ObjectReference

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Endpoint(
    hostname: Option[String] = None,
    topology: Option[Map[String, String]] = None,
    addresses: List[String],
    conditions: Option[EndpointConditions] = None,
    targetRef: Option[ObjectReference] = None
)

object Endpoint {
  implicit val `io.k8s.api.discovery.v1alpha1.Endpoint-Decoder`
      : Decoder[Endpoint] = deriveDecoder
  implicit val `io.k8s.api.discovery.v1alpha1.Endpoint-Encoder`
      : Encoder[Endpoint] = deriveEncoder
}
