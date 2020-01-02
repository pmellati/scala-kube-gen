package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NamespaceStatus(
    conditions: Option[List[NamespaceCondition]] = None,
    phase: Option[String] = None
)

object NamespaceStatus {
  implicit val `io.k8s.api.core.v1.NamespaceStatus-Decoder`
      : Decoder[NamespaceStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NamespaceStatus-Encoder`
      : Encoder[NamespaceStatus] = deriveEncoder
}
