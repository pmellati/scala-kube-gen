package kubeclient.io.k8s.api.storage.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CSINodeList(
    apiVersion: Option[String] = None,
    items: List[CSINode],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object CSINodeList {
  implicit val `io.k8s.api.storage.v1beta1.CSINodeList-Decoder`
      : Decoder[CSINodeList] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.CSINodeList-Encoder`
      : Encoder[CSINodeList] = deriveEncoder
}
