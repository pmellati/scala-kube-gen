package kubeclient.io.k8s.api.apps.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicaSetList(
    apiVersion: Option[String] = None,
    items: List[ReplicaSet],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ReplicaSetList {
  implicit val `io.k8s.api.apps.v1.ReplicaSetList-Decoder`
      : Decoder[ReplicaSetList] = deriveDecoder
  implicit val `io.k8s.api.apps.v1.ReplicaSetList-Encoder`
      : Encoder[ReplicaSetList] = deriveEncoder
}
