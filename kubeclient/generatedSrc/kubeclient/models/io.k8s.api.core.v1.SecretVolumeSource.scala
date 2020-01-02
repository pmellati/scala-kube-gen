package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SecretVolumeSource(
    defaultMode: Option[Int] = None,
    items: Option[List[KeyToPath]] = None,
    optional: Option[Boolean] = None,
    secretName: Option[String] = None
)

object SecretVolumeSource {
  implicit val `io.k8s.api.core.v1.SecretVolumeSource-Decoder`
      : Decoder[SecretVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.SecretVolumeSource-Encoder`
      : Encoder[SecretVolumeSource] = deriveEncoder
}
