package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PersistentVolumeClaimCondition(
    lastProbeTime: Option[Time] = None,
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object PersistentVolumeClaimCondition {
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimCondition-Decoder`
      : Decoder[PersistentVolumeClaimCondition] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PersistentVolumeClaimCondition-Encoder`
      : Encoder[PersistentVolumeClaimCondition] = deriveEncoder
}
