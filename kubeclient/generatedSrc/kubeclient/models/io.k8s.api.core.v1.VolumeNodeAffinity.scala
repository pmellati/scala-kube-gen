package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeNodeAffinity(
    required: Option[NodeSelector] = None
)

object VolumeNodeAffinity {
  implicit val `io.k8s.api.core.v1.VolumeNodeAffinity-Decoder`
      : Decoder[VolumeNodeAffinity] = deriveDecoder
  implicit val `io.k8s.api.core.v1.VolumeNodeAffinity-Encoder`
      : Encoder[VolumeNodeAffinity] = deriveEncoder
}
