package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.util.intstr.IntOrString

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ServicePort(
    nodePort: Option[Int] = None,
    name: Option[String] = None,
    port: Int,
    targetPort: Option[IntOrString] = None,
    protocol: Option[String] = None
)

object ServicePort {
  implicit val `io.k8s.api.core.v1.ServicePort-Decoder`: Decoder[ServicePort] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.ServicePort-Encoder`: Encoder[ServicePort] =
    deriveEncoder
}
