package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SELinuxOptions(
    level: Option[String] = None,
    role: Option[String] = None,
    `type`: Option[String] = None,
    user: Option[String] = None
)

object SELinuxOptions {
  implicit val `io.k8s.api.core.v1.SELinuxOptions-Decoder`
      : Decoder[SELinuxOptions] = deriveDecoder
  implicit val `io.k8s.api.core.v1.SELinuxOptions-Encoder`
      : Encoder[SELinuxOptions] = deriveEncoder
}
