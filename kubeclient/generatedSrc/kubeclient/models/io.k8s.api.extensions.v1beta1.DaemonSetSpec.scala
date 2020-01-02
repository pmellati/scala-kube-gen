package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.api.core.v1.PodTemplateSpec
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DaemonSetSpec(
    minReadySeconds: Option[Int] = None,
    revisionHistoryLimit: Option[Int] = None,
    template: PodTemplateSpec,
    selector: Option[LabelSelector] = None,
    templateGeneration: Option[Long] = None,
    updateStrategy: Option[DaemonSetUpdateStrategy] = None
)

object DaemonSetSpec {
  implicit val `io.k8s.api.extensions.v1beta1.DaemonSetSpec-Decoder`
      : Decoder[DaemonSetSpec] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.DaemonSetSpec-Encoder`
      : Encoder[DaemonSetSpec] = deriveEncoder
}
