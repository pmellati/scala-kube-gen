package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ServiceSpec(
    externalTrafficPolicy: Option[String] = None,
    loadBalancerIP: Option[String] = None,
    clusterIP: Option[String] = None,
    ports: Option[List[ServicePort]] = None,
    healthCheckNodePort: Option[Int] = None,
    externalIPs: Option[List[String]] = None,
    externalName: Option[String] = None,
    `type`: Option[String] = None,
    publishNotReadyAddresses: Option[Boolean] = None,
    sessionAffinityConfig: Option[SessionAffinityConfig] = None,
    loadBalancerSourceRanges: Option[List[String]] = None,
    sessionAffinity: Option[String] = None,
    selector: Option[Map[String, String]] = None,
    ipFamily: Option[String] = None
)

object ServiceSpec {
  implicit val `io.k8s.api.core.v1.ServiceSpec-Decoder`: Decoder[ServiceSpec] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.ServiceSpec-Encoder`: Encoder[ServiceSpec] =
    deriveEncoder
}
