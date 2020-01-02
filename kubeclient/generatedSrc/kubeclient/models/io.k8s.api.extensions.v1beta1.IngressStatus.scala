package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.api.core.v1.LoadBalancerStatus

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class IngressStatus(
    loadBalancer: Option[LoadBalancerStatus] = None
)

object IngressStatus {
  implicit val `io.k8s.api.extensions.v1beta1.IngressStatus-Decoder`
      : Decoder[IngressStatus] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.IngressStatus-Encoder`
      : Encoder[IngressStatus] = deriveEncoder
}
