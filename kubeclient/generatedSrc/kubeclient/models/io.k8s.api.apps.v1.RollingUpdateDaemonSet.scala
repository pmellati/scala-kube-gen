package kubeclient.io.k8s.api.apps.v1

import kubeclient.io.k8s.apimachinery.pkg.util.intstr.IntOrString

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RollingUpdateDaemonSet(
    maxUnavailable: Option[IntOrString] = None
)

object RollingUpdateDaemonSet {
  implicit val `io.k8s.api.apps.v1.RollingUpdateDaemonSet-Decoder`
      : Decoder[RollingUpdateDaemonSet] = deriveDecoder
  implicit val `io.k8s.api.apps.v1.RollingUpdateDaemonSet-Encoder`
      : Encoder[RollingUpdateDaemonSet] = deriveEncoder
}
