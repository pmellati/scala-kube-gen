package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NamespaceList(
    apiVersion: Option[String] = None,
    items: List[Namespace],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object NamespaceList {
  implicit val `io.k8s.api.core.v1.NamespaceList-Decoder`
      : Decoder[NamespaceList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NamespaceList-Encoder`
      : Encoder[NamespaceList] = deriveEncoder
}
