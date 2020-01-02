package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PersistentVolumeClaimStatus(
    accessModes: Option[List[String]] = None,
    capacity: Option[Map[String, Quantity]] = None,
    conditions: Option[List[PersistentVolumeClaimCondition]] = None,
    phase: Option[String] = None
)

object PersistentVolumeClaimStatus {
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimStatus-Decoder`
      : Decoder[PersistentVolumeClaimStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimStatus-Encoder`
      : Encoder[PersistentVolumeClaimStatus] = deriveEncoder
}
