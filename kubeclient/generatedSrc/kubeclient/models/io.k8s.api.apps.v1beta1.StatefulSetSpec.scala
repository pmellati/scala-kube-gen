package kubeclient.io.k8s.api.apps.v1beta1

import kubeclient.io.k8s.api.core.v1.PersistentVolumeClaim
import kubeclient.io.k8s.api.core.v1.PodTemplateSpec
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StatefulSetSpec(
    revisionHistoryLimit: Option[Int] = None,
    podManagementPolicy: Option[String] = None,
    volumeClaimTemplates: Option[List[PersistentVolumeClaim]] = None,
    template: PodTemplateSpec,
    selector: Option[LabelSelector] = None,
    updateStrategy: Option[StatefulSetUpdateStrategy] = None,
    serviceName: String,
    replicas: Option[Int] = None
)

object StatefulSetSpec {
  implicit val `io.k8s.api.apps.v1beta1.StatefulSetSpec-Decoder`
      : Decoder[StatefulSetSpec] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta1.StatefulSetSpec-Encoder`
      : Encoder[StatefulSetSpec] = deriveEncoder
}
