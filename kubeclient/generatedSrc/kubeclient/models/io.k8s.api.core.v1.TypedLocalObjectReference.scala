package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TypedLocalObjectReference(
    apiGroup: Option[String] = None,
    kind: String,
    name: String
)

object TypedLocalObjectReference {
  implicit val `io.k8s.api.core.v1.TypedLocalObjectReference-Decoder`
      : Decoder[TypedLocalObjectReference] = deriveDecoder
  implicit val `io.k8s.api.core.v1.TypedLocalObjectReference-Encoder`
      : Encoder[TypedLocalObjectReference] = deriveEncoder
}
