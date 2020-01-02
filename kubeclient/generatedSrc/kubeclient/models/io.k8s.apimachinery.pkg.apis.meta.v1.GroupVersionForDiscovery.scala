package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class GroupVersionForDiscovery(
    groupVersion: String,
    version: String
)

object GroupVersionForDiscovery {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.GroupVersionForDiscovery-Decoder`
      : Decoder[GroupVersionForDiscovery] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.GroupVersionForDiscovery-Encoder`
      : Encoder[GroupVersionForDiscovery] = deriveEncoder
}
