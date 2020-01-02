package kubeclient.io.k8s.api.apps.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicaSetStatus(
    conditions: Option[List[ReplicaSetCondition]] = None,
    replicas: Int,
    availableReplicas: Option[Int] = None,
    observedGeneration: Option[Long] = None,
    readyReplicas: Option[Int] = None,
    fullyLabeledReplicas: Option[Int] = None
)

object ReplicaSetStatus {
  implicit val `io.k8s.api.apps.v1.ReplicaSetStatus-Decoder`
      : Decoder[ReplicaSetStatus] = deriveDecoder
  implicit val `io.k8s.api.apps.v1.ReplicaSetStatus-Encoder`
      : Encoder[ReplicaSetStatus] = deriveEncoder
}
