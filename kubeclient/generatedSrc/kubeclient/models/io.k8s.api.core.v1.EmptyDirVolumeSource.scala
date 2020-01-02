package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EmptyDirVolumeSource(
    medium: Option[String] = None,
    sizeLimit: Option[Quantity] = None
)

object EmptyDirVolumeSource {
  implicit val `io.k8s.api.core.v1.EmptyDirVolumeSource-Decoder`
      : Decoder[EmptyDirVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.EmptyDirVolumeSource-Encoder`
      : Encoder[EmptyDirVolumeSource] = deriveEncoder
}
