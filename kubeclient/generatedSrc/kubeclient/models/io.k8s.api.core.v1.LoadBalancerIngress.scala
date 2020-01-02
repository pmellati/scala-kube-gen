package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LoadBalancerIngress(
    hostname: Option[String] = None,
    ip: Option[String] = None
)

object LoadBalancerIngress {
  implicit val `io.k8s.api.core.v1.LoadBalancerIngress-Decoder`
      : Decoder[LoadBalancerIngress] = deriveDecoder
  implicit val `io.k8s.api.core.v1.LoadBalancerIngress-Encoder`
      : Encoder[LoadBalancerIngress] = deriveEncoder
}
