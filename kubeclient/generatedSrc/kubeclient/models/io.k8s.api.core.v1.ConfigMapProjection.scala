package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ConfigMapProjection(
    items: Option[List[KeyToPath]] = None,
    name: Option[String] = None,
    optional: Option[Boolean] = None
)

object ConfigMapProjection {
  implicit val `io.k8s.api.core.v1.ConfigMapProjection-Decoder`
      : Decoder[ConfigMapProjection] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ConfigMapProjection-Encoder`
      : Encoder[ConfigMapProjection] = deriveEncoder
}
