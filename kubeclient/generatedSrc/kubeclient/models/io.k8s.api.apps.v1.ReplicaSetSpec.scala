package kubeclient.io.k8s.api.apps.v1

import kubeclient.io.k8s.api.core.v1.PodTemplateSpec
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicaSetSpec(
    minReadySeconds: Option[Int] = None,
    replicas: Option[Int] = None,
    selector: LabelSelector,
    template: Option[PodTemplateSpec] = None
)

object ReplicaSetSpec {
  implicit val `io.k8s.api.apps.v1.ReplicaSetSpec-Decoder`
      : Decoder[ReplicaSetSpec] = deriveDecoder
  implicit val `io.k8s.api.apps.v1.ReplicaSetSpec-Encoder`
      : Encoder[ReplicaSetSpec] = deriveEncoder
}
