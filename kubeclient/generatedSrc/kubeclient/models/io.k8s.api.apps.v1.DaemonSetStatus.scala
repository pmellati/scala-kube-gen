package kubeclient.io.k8s.api.apps.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DaemonSetStatus(
    conditions: Option[List[DaemonSetCondition]] = None,
    updatedNumberScheduled: Option[Int] = None,
    numberMisscheduled: Int,
    collisionCount: Option[Int] = None,
    currentNumberScheduled: Int,
    numberUnavailable: Option[Int] = None,
    observedGeneration: Option[Long] = None,
    desiredNumberScheduled: Int,
    numberReady: Int,
    numberAvailable: Option[Int] = None
)

object DaemonSetStatus {
  implicit val `io.k8s.api.apps.v1.DaemonSetStatus-Decoder`
      : Decoder[DaemonSetStatus] = deriveDecoder
  implicit val `io.k8s.api.apps.v1.DaemonSetStatus-Encoder`
      : Encoder[DaemonSetStatus] = deriveEncoder
}
