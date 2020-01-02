package kubeclient.io.k8s.api.apps.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ControllerRevisionList(
    apiVersion: Option[String] = None,
    items: List[ControllerRevision],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ControllerRevisionList {
  implicit val `io.k8s.api.apps.v1.ControllerRevisionList-Decoder`
      : Decoder[ControllerRevisionList] = deriveDecoder
  implicit val `io.k8s.api.apps.v1.ControllerRevisionList-Encoder`
      : Encoder[ControllerRevisionList] = deriveEncoder
}
