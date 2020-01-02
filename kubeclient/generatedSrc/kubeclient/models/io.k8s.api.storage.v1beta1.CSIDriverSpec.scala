package kubeclient.io.k8s.api.storage.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CSIDriverSpec(
    attachRequired: Option[Boolean] = None,
    podInfoOnMount: Option[Boolean] = None,
    volumeLifecycleModes: Option[List[String]] = None
)

object CSIDriverSpec {
  implicit val `io.k8s.api.storage.v1beta1.CSIDriverSpec-Decoder`
      : Decoder[CSIDriverSpec] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.CSIDriverSpec-Encoder`
      : Encoder[CSIDriverSpec] = deriveEncoder
}
