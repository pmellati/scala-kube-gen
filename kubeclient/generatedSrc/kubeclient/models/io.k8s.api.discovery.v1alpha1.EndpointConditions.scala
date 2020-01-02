package kubeclient.io.k8s.api.discovery.v1alpha1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EndpointConditions(
    ready: Option[Boolean] = None
)

object EndpointConditions {
  implicit val `io.k8s.api.discovery.v1alpha1.EndpointConditions-Decoder`
      : Decoder[EndpointConditions] = deriveDecoder
  implicit val `io.k8s.api.discovery.v1alpha1.EndpointConditions-Encoder`
      : Encoder[EndpointConditions] = deriveEncoder
}
