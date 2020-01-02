package kubeclient.io.k8s.api.events.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EventList(
    apiVersion: Option[String] = None,
    items: List[Event],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object EventList {
  implicit val `io.k8s.api.events.v1beta1.EventList-Decoder`
      : Decoder[EventList] = deriveDecoder
  implicit val `io.k8s.api.events.v1beta1.EventList-Encoder`
      : Encoder[EventList] = deriveEncoder
}
