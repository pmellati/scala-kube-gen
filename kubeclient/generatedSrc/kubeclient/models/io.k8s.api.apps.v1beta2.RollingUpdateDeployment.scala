package kubeclient.io.k8s.api.apps.v1beta2

import kubeclient.io.k8s.apimachinery.pkg.util.intstr.IntOrString

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RollingUpdateDeployment(
    maxSurge: Option[IntOrString] = None,
    maxUnavailable: Option[IntOrString] = None
)

object RollingUpdateDeployment {
  implicit val `io.k8s.api.apps.v1beta2.RollingUpdateDeployment-Decoder`
      : Decoder[RollingUpdateDeployment] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta2.RollingUpdateDeployment-Encoder`
      : Encoder[RollingUpdateDeployment] = deriveEncoder
}
