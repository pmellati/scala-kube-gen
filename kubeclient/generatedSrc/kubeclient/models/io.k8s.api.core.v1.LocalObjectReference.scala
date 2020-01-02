package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LocalObjectReference(
    name: Option[String] = None
)

object LocalObjectReference {
  implicit val `io.k8s.api.core.v1.LocalObjectReference-Decoder`
      : Decoder[LocalObjectReference] = deriveDecoder
  implicit val `io.k8s.api.core.v1.LocalObjectReference-Encoder`
      : Encoder[LocalObjectReference] = deriveEncoder
}
