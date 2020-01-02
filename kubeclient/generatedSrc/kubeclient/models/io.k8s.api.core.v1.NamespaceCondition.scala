package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NamespaceCondition(
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object NamespaceCondition {
  implicit val `io.k8s.api.core.v1.NamespaceCondition-Decoder`
      : Decoder[NamespaceCondition] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NamespaceCondition-Encoder`
      : Encoder[NamespaceCondition] = deriveEncoder
}
