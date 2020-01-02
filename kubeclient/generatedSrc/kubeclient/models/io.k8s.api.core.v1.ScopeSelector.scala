package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ScopeSelector(
    matchExpressions: Option[List[ScopedResourceSelectorRequirement]] = None
)

object ScopeSelector {
  implicit val `io.k8s.api.core.v1.ScopeSelector-Decoder`
      : Decoder[ScopeSelector] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ScopeSelector-Encoder`
      : Encoder[ScopeSelector] = deriveEncoder
}
