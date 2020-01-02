package kubeclient.io.k8s.api.storage.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeNodeResources(
    count: Option[Int] = None
)

object VolumeNodeResources {
  implicit val `io.k8s.api.storage.v1beta1.VolumeNodeResources-Decoder`
      : Decoder[VolumeNodeResources] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.VolumeNodeResources-Encoder`
      : Encoder[VolumeNodeResources] = deriveEncoder
}
