package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EventSource(
    component: Option[String] = None,
    host: Option[String] = None
)

object EventSource {
  implicit val `io.k8s.api.core.v1.EventSource-Decoder`: Decoder[EventSource] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.EventSource-Encoder`: Encoder[EventSource] =
    deriveEncoder
}
