package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ContainerState(
    running: Option[ContainerStateRunning] = None,
    terminated: Option[ContainerStateTerminated] = None,
    waiting: Option[ContainerStateWaiting] = None
)

object ContainerState {
  implicit val `io.k8s.api.core.v1.ContainerState-Decoder`
      : Decoder[ContainerState] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ContainerState-Encoder`
      : Encoder[ContainerState] = deriveEncoder
}
