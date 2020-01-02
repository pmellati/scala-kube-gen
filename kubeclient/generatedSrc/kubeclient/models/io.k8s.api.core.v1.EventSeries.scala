package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.MicroTime

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EventSeries(
    count: Option[Int] = None,
    lastObservedTime: Option[MicroTime] = None,
    state: Option[String] = None
)

object EventSeries {
  implicit val `io.k8s.api.core.v1.EventSeries-Decoder`: Decoder[EventSeries] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.EventSeries-Encoder`: Encoder[EventSeries] =
    deriveEncoder
}
