package kubeclient.io.k8s.api.storage.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CSINodeDriver(
    allocatable: Option[VolumeNodeResources] = None,
    name: String,
    nodeID: String,
    topologyKeys: Option[List[String]] = None
)

object CSINodeDriver {
  implicit val `io.k8s.api.storage.v1beta1.CSINodeDriver-Decoder`
      : Decoder[CSINodeDriver] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.CSINodeDriver-Encoder`
      : Encoder[CSINodeDriver] = deriveEncoder
}
