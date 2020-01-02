package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AttachedVolume(
    devicePath: String,
    name: String
)

object AttachedVolume {
  implicit val `io.k8s.api.core.v1.AttachedVolume-Decoder`
      : Decoder[AttachedVolume] = deriveDecoder
  implicit val `io.k8s.api.core.v1.AttachedVolume-Encoder`
      : Encoder[AttachedVolume] = deriveEncoder
}
