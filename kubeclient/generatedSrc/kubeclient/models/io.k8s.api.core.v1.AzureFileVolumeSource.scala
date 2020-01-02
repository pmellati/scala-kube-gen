package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AzureFileVolumeSource(
    readOnly: Option[Boolean] = None,
    secretName: String,
    shareName: String
)

object AzureFileVolumeSource {
  implicit val `io.k8s.api.core.v1.AzureFileVolumeSource-Decoder`
      : Decoder[AzureFileVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.AzureFileVolumeSource-Encoder`
      : Encoder[AzureFileVolumeSource] = deriveEncoder
}
