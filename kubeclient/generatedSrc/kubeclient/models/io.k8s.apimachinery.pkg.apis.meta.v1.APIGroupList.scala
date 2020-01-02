package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIGroupList(
    apiVersion: Option[String] = None,
    groups: List[APIGroup],
    kind: Option[String] = None
)

object APIGroupList {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIGroupList-Decoder`
      : Decoder[APIGroupList] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIGroupList-Encoder`
      : Encoder[APIGroupList] = deriveEncoder
}
