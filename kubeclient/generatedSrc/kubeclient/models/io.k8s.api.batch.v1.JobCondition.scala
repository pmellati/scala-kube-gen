package kubeclient.io.k8s.api.batch.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class JobCondition(
    lastProbeTime: Option[Time] = None,
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object JobCondition {
  implicit val `io.k8s.api.batch.v1.JobCondition-Decoder`
      : Decoder[JobCondition] = deriveDecoder
  implicit val `io.k8s.api.batch.v1.JobCondition-Encoder`
      : Encoder[JobCondition] = deriveEncoder
}
