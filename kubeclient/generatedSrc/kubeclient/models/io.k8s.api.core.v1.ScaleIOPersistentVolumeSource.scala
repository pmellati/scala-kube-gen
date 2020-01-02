package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ScaleIOPersistentVolumeSource(
    fsType: Option[String] = None,
    secretRef: SecretReference,
    storagePool: Option[String] = None,
    gateway: String,
    sslEnabled: Option[Boolean] = None,
    storageMode: Option[String] = None,
    system: String,
    readOnly: Option[Boolean] = None,
    protectionDomain: Option[String] = None,
    volumeName: Option[String] = None
)

object ScaleIOPersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.ScaleIOPersistentVolumeSource-Decoder`
      : Decoder[ScaleIOPersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ScaleIOPersistentVolumeSource-Encoder`
      : Encoder[ScaleIOPersistentVolumeSource] = deriveEncoder
}
