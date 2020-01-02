package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIResourceList(
    apiVersion: Option[String] = None,
    groupVersion: String,
    kind: Option[String] = None,
    resources: List[APIResource]
)

object APIResourceList {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIResourceList-Decoder`
      : Decoder[APIResourceList] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIResourceList-Encoder`
      : Encoder[APIResourceList] = deriveEncoder
}
