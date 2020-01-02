package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeProjection(
    configMap: Option[ConfigMapProjection] = None,
    downwardAPI: Option[DownwardAPIProjection] = None,
    secret: Option[SecretProjection] = None,
    serviceAccountToken: Option[ServiceAccountTokenProjection] = None
)

object VolumeProjection {
  implicit val `io.k8s.api.core.v1.VolumeProjection-Decoder`
      : Decoder[VolumeProjection] = deriveDecoder
  implicit val `io.k8s.api.core.v1.VolumeProjection-Encoder`
      : Encoder[VolumeProjection] = deriveEncoder
}
