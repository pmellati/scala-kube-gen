package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DeploymentStatus(
    conditions: Option[List[DeploymentCondition]] = None,
    availableReplicas: Option[Int] = None,
    unavailableReplicas: Option[Int] = None,
    observedGeneration: Option[Long] = None,
    collisionCount: Option[Int] = None,
    replicas: Option[Int] = None,
    updatedReplicas: Option[Int] = None,
    readyReplicas: Option[Int] = None
)

object DeploymentStatus {
  implicit val `io.k8s.api.extensions.v1beta1.DeploymentStatus-Decoder`
      : Decoder[DeploymentStatus] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.DeploymentStatus-Encoder`
      : Encoder[DeploymentStatus] = deriveEncoder
}
