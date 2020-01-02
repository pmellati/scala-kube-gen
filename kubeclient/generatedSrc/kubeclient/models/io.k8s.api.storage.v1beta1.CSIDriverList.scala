package kubeclient.io.k8s.api.storage.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CSIDriverList(
    apiVersion: Option[String] = None,
    items: List[CSIDriver],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object CSIDriverList {
  implicit val `io.k8s.api.storage.v1beta1.CSIDriverList-Decoder`
      : Decoder[CSIDriverList] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.CSIDriverList-Encoder`
      : Encoder[CSIDriverList] = deriveEncoder
}
