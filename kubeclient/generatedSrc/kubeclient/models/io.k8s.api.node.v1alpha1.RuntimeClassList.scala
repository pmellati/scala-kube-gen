package kubeclient.io.k8s.api.node.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RuntimeClassList(
    apiVersion: Option[String] = None,
    items: List[RuntimeClass],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object RuntimeClassList {
  implicit val `io.k8s.api.node.v1alpha1.RuntimeClassList-Decoder`
      : Decoder[RuntimeClassList] = deriveDecoder
  implicit val `io.k8s.api.node.v1alpha1.RuntimeClassList-Encoder`
      : Encoder[RuntimeClassList] = deriveEncoder
}
