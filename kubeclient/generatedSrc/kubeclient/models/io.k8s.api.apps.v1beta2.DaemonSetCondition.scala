package kubeclient.io.k8s.api.apps.v1beta2

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DaemonSetCondition(
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object DaemonSetCondition {
  implicit val `io.k8s.api.apps.v1beta2.DaemonSetCondition-Decoder`
      : Decoder[DaemonSetCondition] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta2.DaemonSetCondition-Encoder`
      : Encoder[DaemonSetCondition] = deriveEncoder
}
