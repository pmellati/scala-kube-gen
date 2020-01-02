package kubeclient.io.k8s.api.apps.v1beta2

import kubeclient.io.k8s.api.core.v1.PodTemplateSpec
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DeploymentSpec(
    replicas: Option[Int] = None,
    revisionHistoryLimit: Option[Int] = None,
    paused: Option[Boolean] = None,
    strategy: Option[DeploymentStrategy] = None,
    template: PodTemplateSpec,
    selector: LabelSelector,
    minReadySeconds: Option[Int] = None,
    progressDeadlineSeconds: Option[Int] = None
)

object DeploymentSpec {
  implicit val `io.k8s.api.apps.v1beta2.DeploymentSpec-Decoder`
      : Decoder[DeploymentSpec] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta2.DeploymentSpec-Encoder`
      : Encoder[DeploymentSpec] = deriveEncoder
}
