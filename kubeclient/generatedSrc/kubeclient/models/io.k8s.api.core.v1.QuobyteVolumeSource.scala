package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class QuobyteVolumeSource(
    registry: String,
    readOnly: Option[Boolean] = None,
    tenant: Option[String] = None,
    group: Option[String] = None,
    user: Option[String] = None,
    volume: String
)

object QuobyteVolumeSource {
  implicit val `io.k8s.api.core.v1.QuobyteVolumeSource-Decoder`
      : Decoder[QuobyteVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.QuobyteVolumeSource-Encoder`
      : Encoder[QuobyteVolumeSource] = deriveEncoder
}
