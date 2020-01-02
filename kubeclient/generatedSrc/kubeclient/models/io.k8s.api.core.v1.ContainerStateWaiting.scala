package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ContainerStateWaiting(
    message: Option[String] = None,
    reason: Option[String] = None
)

object ContainerStateWaiting {
  implicit val `io.k8s.api.core.v1.ContainerStateWaiting-Decoder`
      : Decoder[ContainerStateWaiting] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ContainerStateWaiting-Encoder`
      : Encoder[ContainerStateWaiting] = deriveEncoder
}
