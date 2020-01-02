package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class VsphereVirtualDiskVolumeSource(
    fsType: Option[String] = None,
    storagePolicyID: Option[String] = None,
    storagePolicyName: Option[String] = None,
    volumePath: String
)

object VsphereVirtualDiskVolumeSource {
  implicit val `io.k8s.api.core.v1.VsphereVirtualDiskVolumeSource-Decoder`
      : Decoder[VsphereVirtualDiskVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.VsphereVirtualDiskVolumeSource-Encoder`
      : Encoder[VsphereVirtualDiskVolumeSource] = deriveEncoder
}
