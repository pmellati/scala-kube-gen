package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SecretProjection(
    items: Option[List[KeyToPath]] = None,
    name: Option[String] = None,
    optional: Option[Boolean] = None
)

object SecretProjection {
  implicit val `io.k8s.api.core.v1.SecretProjection-Decoder`
      : Decoder[SecretProjection] = deriveDecoder
  implicit val `io.k8s.api.core.v1.SecretProjection-Encoder`
      : Encoder[SecretProjection] = deriveEncoder
}
