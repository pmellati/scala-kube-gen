package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SecretList(
    apiVersion: Option[String] = None,
    items: List[Secret],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object SecretList {
  implicit val `io.k8s.api.core.v1.SecretList-Decoder`: Decoder[SecretList] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.SecretList-Encoder`: Encoder[SecretList] =
    deriveEncoder
}
