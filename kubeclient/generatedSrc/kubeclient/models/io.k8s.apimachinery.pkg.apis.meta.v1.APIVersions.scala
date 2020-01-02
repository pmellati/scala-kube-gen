package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIVersions(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    serverAddressByClientCIDRs: List[ServerAddressByClientCIDR],
    versions: List[String]
)

object APIVersions {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIVersions-Decoder`
      : Decoder[APIVersions] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIVersions-Encoder`
      : Encoder[APIVersions] = deriveEncoder
}
