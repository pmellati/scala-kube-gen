package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class FlockerVolumeSource(
    datasetName: Option[String] = None,
    datasetUUID: Option[String] = None
)

object FlockerVolumeSource {
  implicit val `io.k8s.api.core.v1.FlockerVolumeSource-Decoder`
      : Decoder[FlockerVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.FlockerVolumeSource-Encoder`
      : Encoder[FlockerVolumeSource] = deriveEncoder
}
