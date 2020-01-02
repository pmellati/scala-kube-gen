package kubeclient.io.k8s.api.scheduling.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PriorityClassList(
    apiVersion: Option[String] = None,
    items: List[PriorityClass],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object PriorityClassList {
  implicit val `io.k8s.api.scheduling.v1alpha1.PriorityClassList-Decoder`
      : Decoder[PriorityClassList] = deriveDecoder
  implicit val `io.k8s.api.scheduling.v1alpha1.PriorityClassList-Encoder`
      : Encoder[PriorityClassList] = deriveEncoder
}
