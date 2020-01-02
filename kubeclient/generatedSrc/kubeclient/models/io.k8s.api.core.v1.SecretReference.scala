package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SecretReference(
    name: Option[String] = None,
    namespace: Option[String] = None
)

object SecretReference {
  implicit val `io.k8s.api.core.v1.SecretReference-Decoder`
      : Decoder[SecretReference] = deriveDecoder
  implicit val `io.k8s.api.core.v1.SecretReference-Encoder`
      : Encoder[SecretReference] = deriveEncoder
}
