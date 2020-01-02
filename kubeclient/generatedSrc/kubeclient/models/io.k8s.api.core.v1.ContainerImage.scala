package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ContainerImage(
    names: List[String],
    sizeBytes: Option[Long] = None
)

object ContainerImage {
  implicit val `io.k8s.api.core.v1.ContainerImage-Decoder`
      : Decoder[ContainerImage] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ContainerImage-Encoder`
      : Encoder[ContainerImage] = deriveEncoder
}
