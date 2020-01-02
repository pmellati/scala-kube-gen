package kubeclient.io.k8s.api.storage.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeError(
    message: Option[String] = None,
    time: Option[Time] = None
)

object VolumeError {
  implicit val `io.k8s.api.storage.v1.VolumeError-Decoder`
      : Decoder[VolumeError] = deriveDecoder
  implicit val `io.k8s.api.storage.v1.VolumeError-Encoder`
      : Encoder[VolumeError] = deriveEncoder
}
