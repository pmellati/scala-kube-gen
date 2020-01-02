package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ISCSIVolumeSource(
    portals: Option[List[String]] = None,
    fsType: Option[String] = None,
    initiatorName: Option[String] = None,
    iscsiInterface: Option[String] = None,
    readOnly: Option[Boolean] = None,
    chapAuthDiscovery: Option[Boolean] = None,
    secretRef: Option[LocalObjectReference] = None,
    iqn: String,
    targetPortal: String,
    lun: Int,
    chapAuthSession: Option[Boolean] = None
)

object ISCSIVolumeSource {
  implicit val `io.k8s.api.core.v1.ISCSIVolumeSource-Decoder`
      : Decoder[ISCSIVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ISCSIVolumeSource-Encoder`
      : Encoder[ISCSIVolumeSource] = deriveEncoder
}
