package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DaemonSetUpdateStrategy(
    rollingUpdate: Option[RollingUpdateDaemonSet] = None,
    `type`: Option[String] = None
)

object DaemonSetUpdateStrategy {
  implicit val `io.k8s.api.extensions.v1beta1.DaemonSetUpdateStrategy-Decoder`
      : Decoder[DaemonSetUpdateStrategy] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.DaemonSetUpdateStrategy-Encoder`
      : Encoder[DaemonSetUpdateStrategy] = deriveEncoder
}
