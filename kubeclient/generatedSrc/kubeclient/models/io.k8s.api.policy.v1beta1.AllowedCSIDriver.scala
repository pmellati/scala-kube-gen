package kubeclient.io.k8s.api.policy.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AllowedCSIDriver(
    name: String
)

object AllowedCSIDriver {
  implicit val `io.k8s.api.policy.v1beta1.AllowedCSIDriver-Decoder`
      : Decoder[AllowedCSIDriver] = deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.AllowedCSIDriver-Encoder`
      : Encoder[AllowedCSIDriver] = deriveEncoder
}
