package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PersistentVolumeList(
    apiVersion: Option[String] = None,
    items: List[PersistentVolume],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object PersistentVolumeList {
  implicit val `io.k8s.api.core.v1.PersistentVolumeList-Decoder`
      : Decoder[PersistentVolumeList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PersistentVolumeList-Encoder`
      : Encoder[PersistentVolumeList] = deriveEncoder
}
