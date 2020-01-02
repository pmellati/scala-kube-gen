package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodCondition(
    lastProbeTime: Option[Time] = None,
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object PodCondition {
  implicit val `io.k8s.api.core.v1.PodCondition-Decoder`
      : Decoder[PodCondition] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PodCondition-Encoder`
      : Encoder[PodCondition] = deriveEncoder
}
