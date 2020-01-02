package kubeclient.io.k8s.api.events.v1beta1

import kubeclient.io.k8s.api.core.v1.EventSource
import kubeclient.io.k8s.api.core.v1.ObjectReference
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.MicroTime
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Event(
    regarding: Option[ObjectReference] = None,
    series: Option[EventSeries] = None,
    reason: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    deprecatedLastTimestamp: Option[Time] = None,
    reportingInstance: Option[String] = None,
    action: Option[String] = None,
    deprecatedCount: Option[Int] = None,
    related: Option[ObjectReference] = None,
    deprecatedFirstTimestamp: Option[Time] = None,
    note: Option[String] = None,
    reportingController: Option[String] = None,
    eventTime: MicroTime,
    kind: Option[String] = None,
    deprecatedSource: Option[EventSource] = None,
    apiVersion: Option[String] = None,
    `type`: Option[String] = None
)

object Event {
  implicit val `io.k8s.api.events.v1beta1.Event-Decoder`: Decoder[Event] =
    deriveDecoder
  implicit val `io.k8s.api.events.v1beta1.Event-Encoder`: Encoder[Event] =
    deriveEncoder
}
