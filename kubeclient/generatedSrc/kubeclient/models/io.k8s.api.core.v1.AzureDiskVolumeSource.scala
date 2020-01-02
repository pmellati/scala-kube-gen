package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AzureDiskVolumeSource(
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    diskURI: String,
    cachingMode: Option[String] = None,
    kind: Option[String] = None,
    diskName: String
)

object AzureDiskVolumeSource {
  implicit val `io.k8s.api.core.v1.AzureDiskVolumeSource-Decoder`
      : Decoder[AzureDiskVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.AzureDiskVolumeSource-Encoder`
      : Encoder[AzureDiskVolumeSource] = deriveEncoder
}
