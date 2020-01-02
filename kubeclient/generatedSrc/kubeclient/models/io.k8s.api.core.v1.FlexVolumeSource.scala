package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class FlexVolumeSource(
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    secretRef: Option[LocalObjectReference] = None,
    options: Option[Map[String, String]] = None,
    driver: String
)

object FlexVolumeSource {
  implicit val `io.k8s.api.core.v1.FlexVolumeSource-Decoder`
      : Decoder[FlexVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.FlexVolumeSource-Encoder`
      : Encoder[FlexVolumeSource] = deriveEncoder
}
