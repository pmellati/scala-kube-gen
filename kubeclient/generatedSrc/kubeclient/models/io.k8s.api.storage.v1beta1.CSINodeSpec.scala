package kubeclient.io.k8s.api.storage.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CSINodeSpec(
    drivers: List[CSINodeDriver]
)

object CSINodeSpec {
  implicit val `io.k8s.api.storage.v1beta1.CSINodeSpec-Decoder`
      : Decoder[CSINodeSpec] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.CSINodeSpec-Encoder`
      : Encoder[CSINodeSpec] = deriveEncoder
}
