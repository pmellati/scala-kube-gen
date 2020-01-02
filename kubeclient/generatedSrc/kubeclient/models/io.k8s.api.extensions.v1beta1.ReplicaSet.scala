package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicaSet(
    status: Option[ReplicaSetStatus] = None,
    spec: Option[ReplicaSetSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object ReplicaSet {
  implicit val `io.k8s.api.extensions.v1beta1.ReplicaSet-Decoder`
      : Decoder[ReplicaSet] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.ReplicaSet-Encoder`
      : Encoder[ReplicaSet] = deriveEncoder
}
