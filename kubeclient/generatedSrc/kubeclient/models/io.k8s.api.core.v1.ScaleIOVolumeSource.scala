package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ScaleIOVolumeSource(
    fsType: Option[String] = None,
    secretRef: LocalObjectReference,
    storagePool: Option[String] = None,
    gateway: String,
    sslEnabled: Option[Boolean] = None,
    storageMode: Option[String] = None,
    system: String,
    readOnly: Option[Boolean] = None,
    protectionDomain: Option[String] = None,
    volumeName: Option[String] = None
)

object ScaleIOVolumeSource {
  implicit val `io.k8s.api.core.v1.ScaleIOVolumeSource-Decoder`
      : Decoder[ScaleIOVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ScaleIOVolumeSource-Encoder`
      : Encoder[ScaleIOVolumeSource] = deriveEncoder
}
