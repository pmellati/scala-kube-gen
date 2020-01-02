package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodList(
    apiVersion: Option[String] = None,
    items: List[Pod],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object PodList {
  implicit val `io.k8s.api.core.v1.PodList-Decoder`: Decoder[PodList] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.PodList-Encoder`: Encoder[PodList] =
    deriveEncoder
}
