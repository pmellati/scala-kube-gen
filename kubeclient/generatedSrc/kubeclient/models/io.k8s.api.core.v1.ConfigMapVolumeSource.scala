package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ConfigMapVolumeSource(
    defaultMode: Option[Int] = None,
    items: Option[List[KeyToPath]] = None,
    name: Option[String] = None,
    optional: Option[Boolean] = None
)

object ConfigMapVolumeSource {
  implicit val `io.k8s.api.core.v1.ConfigMapVolumeSource-Decoder`
      : Decoder[ConfigMapVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ConfigMapVolumeSource-Encoder`
      : Encoder[ConfigMapVolumeSource] = deriveEncoder
}
