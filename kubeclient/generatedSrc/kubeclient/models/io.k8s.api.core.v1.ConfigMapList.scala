package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ConfigMapList(
    apiVersion: Option[String] = None,
    items: List[ConfigMap],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ConfigMapList {
  implicit val `io.k8s.api.core.v1.ConfigMapList-Decoder`
      : Decoder[ConfigMapList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ConfigMapList-Encoder`
      : Encoder[ConfigMapList] = deriveEncoder
}
