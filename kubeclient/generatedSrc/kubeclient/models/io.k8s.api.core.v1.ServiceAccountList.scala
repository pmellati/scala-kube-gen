package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ServiceAccountList(
    apiVersion: Option[String] = None,
    items: List[ServiceAccount],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ServiceAccountList {
  implicit val `io.k8s.api.core.v1.ServiceAccountList-Decoder`
      : Decoder[ServiceAccountList] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ServiceAccountList-Encoder`
      : Encoder[ServiceAccountList] = deriveEncoder
}
