package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ScopedResourceSelectorRequirement(
    operator: String,
    scopeName: String,
    values: Option[List[String]] = None
)

object ScopedResourceSelectorRequirement {
  implicit val `io.k8s.api.core.v1.ScopedResourceSelectorRequirement-Decoder`
      : Decoder[ScopedResourceSelectorRequirement] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ScopedResourceSelectorRequirement-Encoder`
      : Encoder[ScopedResourceSelectorRequirement] = deriveEncoder
}
