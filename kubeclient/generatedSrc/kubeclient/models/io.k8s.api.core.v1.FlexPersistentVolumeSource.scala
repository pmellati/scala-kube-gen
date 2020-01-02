package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class FlexPersistentVolumeSource(
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    secretRef: Option[SecretReference] = None,
    options: Option[Map[String, String]] = None,
    driver: String
)

object FlexPersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.FlexPersistentVolumeSource-Decoder`
      : Decoder[FlexPersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.FlexPersistentVolumeSource-Encoder`
      : Encoder[FlexPersistentVolumeSource] = deriveEncoder
}
