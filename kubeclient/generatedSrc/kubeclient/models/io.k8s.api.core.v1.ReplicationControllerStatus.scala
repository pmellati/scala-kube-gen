package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicationControllerStatus(
    conditions: Option[List[ReplicationControllerCondition]] = None,
    replicas: Int,
    availableReplicas: Option[Int] = None,
    observedGeneration: Option[Long] = None,
    readyReplicas: Option[Int] = None,
    fullyLabeledReplicas: Option[Int] = None
)

object ReplicationControllerStatus {
  implicit val `io.k8s.api.core.v1.ReplicationControllerStatus-Decoder`
      : Decoder[ReplicationControllerStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ReplicationControllerStatus-Encoder`
      : Encoder[ReplicationControllerStatus] = deriveEncoder
}
