package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceQuotaSpec(
    hard: Option[Map[String, Quantity]] = None,
    scopeSelector: Option[ScopeSelector] = None,
    scopes: Option[List[String]] = None
)

object ResourceQuotaSpec {
  implicit val `io.k8s.api.core.v1.ResourceQuotaSpec-Decoder`
      : Decoder[ResourceQuotaSpec] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ResourceQuotaSpec-Encoder`
      : Encoder[ResourceQuotaSpec] = deriveEncoder
}
