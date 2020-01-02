package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.api.core.v1.PodTemplateSpec
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicaSetSpec(
    minReadySeconds: Option[Int] = None,
    replicas: Option[Int] = None,
    selector: Option[LabelSelector] = None,
    template: Option[PodTemplateSpec] = None
)

object ReplicaSetSpec {
  implicit val `io.k8s.api.extensions.v1beta1.ReplicaSetSpec-Decoder`
      : Decoder[ReplicaSetSpec] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.ReplicaSetSpec-Encoder`
      : Encoder[ReplicaSetSpec] = deriveEncoder
}
