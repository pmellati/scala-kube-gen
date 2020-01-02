package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ServiceList(
    apiVersion: Option[String] = None,
    items: List[Service],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ServiceList {
  implicit val `io.k8s.api.core.v1.ServiceList-Decoder`: Decoder[ServiceList] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.ServiceList-Encoder`: Encoder[ServiceList] =
    deriveEncoder
}
