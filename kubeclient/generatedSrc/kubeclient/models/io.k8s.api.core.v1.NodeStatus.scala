package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeStatus(
    volumesInUse: Option[List[String]] = None,
    volumesAttached: Option[List[AttachedVolume]] = None,
    phase: Option[String] = None,
    config: Option[NodeConfigStatus] = None,
    allocatable: Option[Map[String, Quantity]] = None,
    images: Option[List[ContainerImage]] = None,
    conditions: Option[List[NodeCondition]] = None,
    nodeInfo: Option[NodeSystemInfo] = None,
    daemonEndpoints: Option[NodeDaemonEndpoints] = None,
    addresses: Option[List[NodeAddress]] = None,
    capacity: Option[Map[String, Quantity]] = None
)

object NodeStatus {
  implicit val `io.k8s.api.core.v1.NodeStatus-Decoder`: Decoder[NodeStatus] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeStatus-Encoder`: Encoder[NodeStatus] =
    deriveEncoder
}
