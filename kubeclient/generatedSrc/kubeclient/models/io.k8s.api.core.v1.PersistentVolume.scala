package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PersistentVolume(
    status: Option[PersistentVolumeStatus] = None,
    spec: Option[PersistentVolumeSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object PersistentVolume {
  implicit val `io.k8s.api.core.v1.PersistentVolume-Decoder`
      : Decoder[PersistentVolume] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PersistentVolume-Encoder`
      : Encoder[PersistentVolume] = deriveEncoder
}
