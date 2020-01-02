package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AzureFilePersistentVolumeSource(
    readOnly: Option[Boolean] = None,
    secretName: String,
    secretNamespace: Option[String] = None,
    shareName: String
)

object AzureFilePersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.AzureFilePersistentVolumeSource-Decoder`
      : Decoder[AzureFilePersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.AzureFilePersistentVolumeSource-Encoder`
      : Encoder[AzureFilePersistentVolumeSource] = deriveEncoder
}
