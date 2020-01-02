package kubeclient.io.k8s.api.events.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.MicroTime

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EventSeries(
    count: Int,
    lastObservedTime: MicroTime,
    state: String
)

object EventSeries {
  implicit val `io.k8s.api.events.v1beta1.EventSeries-Decoder`
      : Decoder[EventSeries] = deriveDecoder
  implicit val `io.k8s.api.events.v1beta1.EventSeries-Encoder`
      : Encoder[EventSeries] = deriveEncoder
}
