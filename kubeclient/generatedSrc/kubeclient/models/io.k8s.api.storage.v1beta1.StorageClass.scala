package kubeclient.io.k8s.api.storage.v1beta1

import kubeclient.io.k8s.api.core.v1.TopologySelectorTerm
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StorageClass(
    mountOptions: Option[List[String]] = None,
    reclaimPolicy: Option[String] = None,
    volumeBindingMode: Option[String] = None,
    allowVolumeExpansion: Option[Boolean] = None,
    parameters: Option[Map[String, String]] = None,
    provisioner: String,
    metadata: Option[ObjectMeta] = None,
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    allowedTopologies: Option[List[TopologySelectorTerm]] = None
)

object StorageClass {
  implicit val `io.k8s.api.storage.v1beta1.StorageClass-Decoder`
      : Decoder[StorageClass] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.StorageClass-Encoder`
      : Encoder[StorageClass] = deriveEncoder
}
