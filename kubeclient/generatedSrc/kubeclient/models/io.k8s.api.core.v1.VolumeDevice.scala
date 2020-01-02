package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeDevice(
    devicePath: String,
    name: String
)

object VolumeDevice {
  implicit val `io.k8s.api.core.v1.VolumeDevice-Decoder`
      : Decoder[VolumeDevice] = deriveDecoder
  implicit val `io.k8s.api.core.v1.VolumeDevice-Encoder`
      : Encoder[VolumeDevice] = deriveEncoder
}
