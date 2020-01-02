package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ListMeta(
    continue: Option[String] = None,
    remainingItemCount: Option[Long] = None,
    resourceVersion: Option[String] = None,
    selfLink: Option[String] = None
)

object ListMeta {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta-Decoder`
      : Decoder[ListMeta] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta-Encoder`
      : Encoder[ListMeta] = deriveEncoder
}
