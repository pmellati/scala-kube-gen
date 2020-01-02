package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class IngressList(
    apiVersion: Option[String] = None,
    items: List[Ingress],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object IngressList {
  implicit val `io.k8s.api.extensions.v1beta1.IngressList-Decoder`
      : Decoder[IngressList] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.IngressList-Encoder`
      : Encoder[IngressList] = deriveEncoder
}
