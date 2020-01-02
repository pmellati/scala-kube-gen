package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class GlusterfsPersistentVolumeSource(
    endpoints: String,
    endpointsNamespace: Option[String] = None,
    path: String,
    readOnly: Option[Boolean] = None
)

object GlusterfsPersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.GlusterfsPersistentVolumeSource-Decoder`
      : Decoder[GlusterfsPersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.GlusterfsPersistentVolumeSource-Encoder`
      : Encoder[GlusterfsPersistentVolumeSource] = deriveEncoder
}
