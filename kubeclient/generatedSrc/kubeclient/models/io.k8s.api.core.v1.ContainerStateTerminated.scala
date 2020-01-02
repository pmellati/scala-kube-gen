package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ContainerStateTerminated(
    exitCode: Int,
    reason: Option[String] = None,
    startedAt: Option[Time] = None,
    message: Option[String] = None,
    finishedAt: Option[Time] = None,
    containerID: Option[String] = None,
    signal: Option[Int] = None
)

object ContainerStateTerminated {
  implicit val `io.k8s.api.core.v1.ContainerStateTerminated-Decoder`
      : Decoder[ContainerStateTerminated] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ContainerStateTerminated-Encoder`
      : Encoder[ContainerStateTerminated] = deriveEncoder
}
