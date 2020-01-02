package kubeclient.io.k8s.api.apps.v1beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DaemonSetUpdateStrategy(
    rollingUpdate: Option[RollingUpdateDaemonSet] = None,
    `type`: Option[String] = None
)

object DaemonSetUpdateStrategy {
  implicit val `io.k8s.api.apps.v1beta2.DaemonSetUpdateStrategy-Decoder`
      : Decoder[DaemonSetUpdateStrategy] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta2.DaemonSetUpdateStrategy-Encoder`
      : Encoder[DaemonSetUpdateStrategy] = deriveEncoder
}
