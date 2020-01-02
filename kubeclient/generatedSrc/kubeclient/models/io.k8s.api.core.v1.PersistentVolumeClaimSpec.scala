package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PersistentVolumeClaimSpec(
    accessModes: Option[List[String]] = None,
    volumeMode: Option[String] = None,
    storageClassName: Option[String] = None,
    dataSource: Option[TypedLocalObjectReference] = None,
    volumeName: Option[String] = None,
    resources: Option[ResourceRequirements] = None,
    selector: Option[LabelSelector] = None
)

object PersistentVolumeClaimSpec {
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimSpec-Decoder`
      : Decoder[PersistentVolumeClaimSpec] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimSpec-Encoder`
      : Encoder[PersistentVolumeClaimSpec] = deriveEncoder
}
