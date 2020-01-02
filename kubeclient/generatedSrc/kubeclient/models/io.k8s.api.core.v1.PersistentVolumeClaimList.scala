package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PersistentVolumeClaimList(
    apiVersion: Option[String] = None,
    items: List[PersistentVolumeClaim],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object PersistentVolumeClaimList {
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimList-Decoder`
      : Decoder[PersistentVolumeClaimList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimList-Encoder`
      : Encoder[PersistentVolumeClaimList] = deriveEncoder
}
