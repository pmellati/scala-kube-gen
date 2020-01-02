package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DaemonSetList(
    apiVersion: Option[String] = None,
    items: List[DaemonSet],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object DaemonSetList {
  implicit val `io.k8s.api.extensions.v1beta1.DaemonSetList-Decoder`
      : Decoder[DaemonSetList] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.DaemonSetList-Encoder`
      : Encoder[DaemonSetList] = deriveEncoder
}
