package kubeclient.io.k8s.api.authentication.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class BoundObjectReference(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    name: Option[String] = None,
    uid: Option[String] = None
)

object BoundObjectReference {
  implicit val `io.k8s.api.authentication.v1.BoundObjectReference-Decoder`
      : Decoder[BoundObjectReference] = deriveDecoder
  implicit val `io.k8s.api.authentication.v1.BoundObjectReference-Encoder`
      : Encoder[BoundObjectReference] = deriveEncoder
}
