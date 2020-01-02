package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VolumeMount(
    subPath: Option[String] = None,
    readOnly: Option[Boolean] = None,
    mountPath: String,
    subPathExpr: Option[String] = None,
    name: String,
    mountPropagation: Option[String] = None
)

object VolumeMount {
  implicit val `io.k8s.api.core.v1.VolumeMount-Decoder`: Decoder[VolumeMount] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.VolumeMount-Encoder`: Encoder[VolumeMount] =
    deriveEncoder
}
