package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ContainerStateRunning(
    startedAt: Option[Time] = None
)

object ContainerStateRunning {
  implicit val `io.k8s.api.core.v1.ContainerStateRunning-Decoder`
      : Decoder[ContainerStateRunning] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ContainerStateRunning-Encoder`
      : Encoder[ContainerStateRunning] = deriveEncoder
}
