package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LoadBalancerStatus(
    ingress: Option[List[LoadBalancerIngress]] = None
)

object LoadBalancerStatus {
  implicit val `io.k8s.api.core.v1.LoadBalancerStatus-Decoder`
      : Decoder[LoadBalancerStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.LoadBalancerStatus-Encoder`
      : Encoder[LoadBalancerStatus] = deriveEncoder
}
