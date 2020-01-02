package kubeclient.io.k8s.api.authorization.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NonResourceAttributes(
    path: Option[String] = None,
    verb: Option[String] = None
)

object NonResourceAttributes {
  implicit val `io.k8s.api.authorization.v1beta1.NonResourceAttributes-Decoder`
      : Decoder[NonResourceAttributes] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1beta1.NonResourceAttributes-Encoder`
      : Encoder[NonResourceAttributes] = deriveEncoder
}
