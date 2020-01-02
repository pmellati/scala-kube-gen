package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.util.intstr.IntOrString

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class IngressBackend(
    serviceName: String,
    servicePort: IntOrString
)

object IngressBackend {
  implicit val `io.k8s.api.extensions.v1beta1.IngressBackend-Decoder`
      : Decoder[IngressBackend] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.IngressBackend-Encoder`
      : Encoder[IngressBackend] = deriveEncoder
}
