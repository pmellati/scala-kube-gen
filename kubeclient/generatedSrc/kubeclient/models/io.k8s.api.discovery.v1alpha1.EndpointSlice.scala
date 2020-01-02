package kubeclient.io.k8s.api.discovery.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EndpointSlice(
    endpoints: List[Endpoint],
    ports: Option[List[EndpointPort]] = None,
    addressType: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object EndpointSlice {
  implicit val `io.k8s.api.discovery.v1alpha1.EndpointSlice-Decoder`
      : Decoder[EndpointSlice] = deriveDecoder
  implicit val `io.k8s.api.discovery.v1alpha1.EndpointSlice-Encoder`
      : Encoder[EndpointSlice] = deriveEncoder
}
