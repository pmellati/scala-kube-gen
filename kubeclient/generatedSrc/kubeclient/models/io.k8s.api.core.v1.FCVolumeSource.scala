package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class FCVolumeSource(
    targetWWNs: Option[List[String]] = None,
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    wwids: Option[List[String]] = None,
    lun: Option[Int] = None
)

object FCVolumeSource {
  implicit val `io.k8s.api.core.v1.FCVolumeSource-Decoder`
      : Decoder[FCVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.FCVolumeSource-Encoder`
      : Encoder[FCVolumeSource] = deriveEncoder
}
