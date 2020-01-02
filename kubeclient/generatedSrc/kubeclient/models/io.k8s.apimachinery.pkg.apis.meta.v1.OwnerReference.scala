package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class OwnerReference(
    name: String,
    blockOwnerDeletion: Option[Boolean] = None,
    uid: String,
    controller: Option[Boolean] = None,
    kind: String,
    apiVersion: String
)

object OwnerReference {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.OwnerReference-Decoder`
      : Decoder[OwnerReference] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.OwnerReference-Encoder`
      : Encoder[OwnerReference] = deriveEncoder
}
