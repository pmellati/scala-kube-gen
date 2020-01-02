package kubeclient.io.k8s.api.apps.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DeploymentRollback(
    name: String,
    updatedAnnotations: Option[Map[String, String]] = None,
    rollbackTo: RollbackConfig,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object DeploymentRollback {
  implicit val `io.k8s.api.apps.v1beta1.DeploymentRollback-Decoder`
      : Decoder[DeploymentRollback] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta1.DeploymentRollback-Encoder`
      : Encoder[DeploymentRollback] = deriveEncoder
}
