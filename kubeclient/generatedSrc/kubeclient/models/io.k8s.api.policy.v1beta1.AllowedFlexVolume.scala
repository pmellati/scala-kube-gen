package kubeclient.io.k8s.api.policy.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AllowedFlexVolume(
    driver: String
)

object AllowedFlexVolume {
  implicit val `io.k8s.api.policy.v1beta1.AllowedFlexVolume-Decoder`
      : Decoder[AllowedFlexVolume] = deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.AllowedFlexVolume-Encoder`
      : Encoder[AllowedFlexVolume] = deriveEncoder
}
