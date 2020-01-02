package kubeclient.io.k8s.api.apps.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StatefulSetStatus(
    conditions: Option[List[StatefulSetCondition]] = None,
    observedGeneration: Option[Long] = None,
    currentRevision: Option[String] = None,
    collisionCount: Option[Int] = None,
    replicas: Int,
    currentReplicas: Option[Int] = None,
    updateRevision: Option[String] = None,
    updatedReplicas: Option[Int] = None,
    readyReplicas: Option[Int] = None
)

object StatefulSetStatus {
  implicit val `io.k8s.api.apps.v1.StatefulSetStatus-Decoder`
      : Decoder[StatefulSetStatus] = deriveDecoder
  implicit val `io.k8s.api.apps.v1.StatefulSetStatus-Encoder`
      : Encoder[StatefulSetStatus] = deriveEncoder
}
