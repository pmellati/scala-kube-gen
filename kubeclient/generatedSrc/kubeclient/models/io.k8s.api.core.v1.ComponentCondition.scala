package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ComponentCondition(
    error: Option[String] = None,
    message: Option[String] = None,
    status: String,
    `type`: String
)

object ComponentCondition {
  implicit val `io.k8s.api.core.v1.ComponentCondition-Decoder`
      : Decoder[ComponentCondition] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ComponentCondition-Encoder`
      : Encoder[ComponentCondition] = deriveEncoder
}
