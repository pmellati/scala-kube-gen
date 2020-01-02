package kubeclient.io.k8s.api.storage.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StorageClassList(
    apiVersion: Option[String] = None,
    items: List[StorageClass],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object StorageClassList {
  implicit val `io.k8s.api.storage.v1beta1.StorageClassList-Decoder`
      : Decoder[StorageClassList] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.StorageClassList-Encoder`
      : Encoder[StorageClassList] = deriveEncoder
}
