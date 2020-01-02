package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Capabilities(
    add: Option[List[String]] = None,
    drop: Option[List[String]] = None
)

object Capabilities {
  implicit val `io.k8s.api.core.v1.Capabilities-Decoder`
      : Decoder[Capabilities] = deriveDecoder
  implicit val `io.k8s.api.core.v1.Capabilities-Encoder`
      : Encoder[Capabilities] = deriveEncoder
}
