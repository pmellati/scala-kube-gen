package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ContainerPort(
    name: Option[String] = None,
    containerPort: Int,
    hostPort: Option[Int] = None,
    hostIP: Option[String] = None,
    protocol: Option[String] = None
)

object ContainerPort {
  implicit val `io.k8s.api.core.v1.ContainerPort-Decoder`
      : Decoder[ContainerPort] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ContainerPort-Encoder`
      : Encoder[ContainerPort] = deriveEncoder
}
