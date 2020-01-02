package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ServiceStatus(
    loadBalancer: Option[LoadBalancerStatus] = None
)

object ServiceStatus {
  implicit val `io.k8s.api.core.v1.ServiceStatus-Decoder`
      : Decoder[ServiceStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ServiceStatus-Encoder`
      : Encoder[ServiceStatus] = deriveEncoder
}
