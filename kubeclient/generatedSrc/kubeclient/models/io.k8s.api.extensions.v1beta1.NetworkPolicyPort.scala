package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.util.intstr.IntOrString

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NetworkPolicyPort(
    port: Option[IntOrString] = None,
    protocol: Option[String] = None
)

object NetworkPolicyPort {
  implicit val `io.k8s.api.extensions.v1beta1.NetworkPolicyPort-Decoder`
      : Decoder[NetworkPolicyPort] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.NetworkPolicyPort-Encoder`
      : Encoder[NetworkPolicyPort] = deriveEncoder
}
