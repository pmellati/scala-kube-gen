package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ComponentStatusList(
    apiVersion: Option[String] = None,
    items: List[ComponentStatus],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ComponentStatusList {
  implicit val `io.k8s.api.core.v1.ComponentStatusList-Decoder`
      : Decoder[ComponentStatusList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ComponentStatusList-Encoder`
      : Encoder[ComponentStatusList] = deriveEncoder
}
