package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ContainerStatus(
    name: String,
    state: Option[ContainerState] = None,
    image: String,
    lastState: Option[ContainerState] = None,
    imageID: String,
    containerID: Option[String] = None,
    started: Option[Boolean] = None,
    restartCount: Int,
    ready: Boolean
)

object ContainerStatus {
  implicit val `io.k8s.api.core.v1.ContainerStatus-Decoder`
      : Decoder[ContainerStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ContainerStatus-Encoder`
      : Encoder[ContainerStatus] = deriveEncoder
}
