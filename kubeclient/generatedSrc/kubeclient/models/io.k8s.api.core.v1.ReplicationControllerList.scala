package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicationControllerList(
    apiVersion: Option[String] = None,
    items: List[ReplicationController],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ReplicationControllerList {
  implicit val `io.k8s.api.core.v1.ReplicationControllerList-Decoder`
      : Decoder[ReplicationControllerList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ReplicationControllerList-Encoder`
      : Encoder[ReplicationControllerList] = deriveEncoder
}
