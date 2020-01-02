package kubeclient.io.k8s.api.discovery.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EndpointSliceList(
    apiVersion: Option[String] = None,
    items: List[EndpointSlice],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object EndpointSliceList {
  implicit val `io.k8s.api.discovery.v1alpha1.EndpointSliceList-Decoder`
      : Decoder[EndpointSliceList] = deriveDecoder
  implicit val `io.k8s.api.discovery.v1alpha1.EndpointSliceList-Encoder`
      : Encoder[EndpointSliceList] = deriveEncoder
}
