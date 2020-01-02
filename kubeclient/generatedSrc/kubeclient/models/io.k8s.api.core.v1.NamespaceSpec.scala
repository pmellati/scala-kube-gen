package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NamespaceSpec(
    finalizers: Option[List[String]] = None
)

object NamespaceSpec {
  implicit val `io.k8s.api.core.v1.NamespaceSpec-Decoder`
      : Decoder[NamespaceSpec] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NamespaceSpec-Encoder`
      : Encoder[NamespaceSpec] = deriveEncoder
}
