package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Endpoints(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    subsets: Option[List[EndpointSubset]] = None
)

object Endpoints {
  implicit val `io.k8s.api.core.v1.Endpoints-Decoder`: Decoder[Endpoints] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Endpoints-Encoder`: Encoder[Endpoints] =
    deriveEncoder
}
