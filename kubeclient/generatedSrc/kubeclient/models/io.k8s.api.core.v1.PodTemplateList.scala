package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodTemplateList(
    apiVersion: Option[String] = None,
    items: List[PodTemplate],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object PodTemplateList {
  implicit val `io.k8s.api.core.v1.PodTemplateList-Decoder`
      : Decoder[PodTemplateList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PodTemplateList-Encoder`
      : Encoder[PodTemplateList] = deriveEncoder
}
