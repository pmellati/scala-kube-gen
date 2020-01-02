package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIGroup(
    name: String,
    preferredVersion: Option[GroupVersionForDiscovery] = None,
    versions: List[GroupVersionForDiscovery],
    serverAddressByClientCIDRs: Option[List[ServerAddressByClientCIDR]] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object APIGroup {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIGroup-Decoder`
      : Decoder[APIGroup] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIGroup-Encoder`
      : Encoder[APIGroup] = deriveEncoder
}
