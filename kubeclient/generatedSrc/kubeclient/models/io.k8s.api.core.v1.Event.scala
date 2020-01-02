package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.MicroTime
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Event(
    involvedObject: ObjectReference,
    series: Option[EventSeries] = None,
    count: Option[Int] = None,
    source: Option[EventSource] = None,
    reportingComponent: Option[String] = None,
    reason: Option[String] = None,
    related: Option[ObjectReference] = None,
    eventTime: Option[MicroTime] = None,
    message: Option[String] = None,
    firstTimestamp: Option[Time] = None,
    reportingInstance: Option[String] = None,
    action: Option[String] = None,
    lastTimestamp: Option[Time] = None,
    metadata: ObjectMeta,
    kind: Option[String] = None,
    apiVersion: Option[String] = None,
    `type`: Option[String] = None
)

object Event {
  implicit val `io.k8s.api.core.v1.Event-Decoder`: Decoder[Event] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Event-Encoder`: Encoder[Event] =
    deriveEncoder
}
