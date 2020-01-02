package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EndpointsList(
    apiVersion: Option[String] = None,
    items: List[Endpoints],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object EndpointsList {
  implicit val `io.k8s.api.core.v1.EndpointsList-Decoder`
      : Decoder[EndpointsList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.EndpointsList-Encoder`
      : Encoder[EndpointsList] = deriveEncoder
}
