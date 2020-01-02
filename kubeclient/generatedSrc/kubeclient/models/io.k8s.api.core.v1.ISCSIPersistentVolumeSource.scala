package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ISCSIPersistentVolumeSource(
    portals: Option[List[String]] = None,
    fsType: Option[String] = None,
    initiatorName: Option[String] = None,
    iscsiInterface: Option[String] = None,
    readOnly: Option[Boolean] = None,
    chapAuthDiscovery: Option[Boolean] = None,
    secretRef: Option[SecretReference] = None,
    iqn: String,
    targetPortal: String,
    lun: Int,
    chapAuthSession: Option[Boolean] = None
)

object ISCSIPersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.ISCSIPersistentVolumeSource-Decoder`
      : Decoder[ISCSIPersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ISCSIPersistentVolumeSource-Encoder`
      : Encoder[ISCSIPersistentVolumeSource] = deriveEncoder
}
