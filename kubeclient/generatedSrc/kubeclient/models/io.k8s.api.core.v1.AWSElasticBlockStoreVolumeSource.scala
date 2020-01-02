package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AWSElasticBlockStoreVolumeSource(
    fsType: Option[String] = None,
    partition: Option[Int] = None,
    readOnly: Option[Boolean] = None,
    volumeID: String
)

object AWSElasticBlockStoreVolumeSource {
  implicit val `io.k8s.api.core.v1.AWSElasticBlockStoreVolumeSource-Decoder`
      : Decoder[AWSElasticBlockStoreVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.AWSElasticBlockStoreVolumeSource-Encoder`
      : Encoder[AWSElasticBlockStoreVolumeSource] = deriveEncoder
}
