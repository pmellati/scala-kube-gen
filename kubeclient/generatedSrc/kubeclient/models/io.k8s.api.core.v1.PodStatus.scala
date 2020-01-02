package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodStatus(
    conditions: Option[List[PodCondition]] = None,
    qosClass: Option[String] = None,
    startTime: Option[Time] = None,
    phase: Option[String] = None,
    reason: Option[String] = None,
    ephemeralContainerStatuses: Option[List[ContainerStatus]] = None,
    podIP: Option[String] = None,
    nominatedNodeName: Option[String] = None,
    hostIP: Option[String] = None,
    message: Option[String] = None,
    containerStatuses: Option[List[ContainerStatus]] = None,
    podIPs: Option[List[PodIP]] = None,
    initContainerStatuses: Option[List[ContainerStatus]] = None
)

object PodStatus {
  implicit val `io.k8s.api.core.v1.PodStatus-Decoder`: Decoder[PodStatus] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.PodStatus-Encoder`: Encoder[PodStatus] =
    deriveEncoder
}
