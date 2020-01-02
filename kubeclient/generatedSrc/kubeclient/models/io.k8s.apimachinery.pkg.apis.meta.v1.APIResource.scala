package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIResource(
    name: String,
    verbs: List[String],
    version: Option[String] = None,
    categories: Option[List[String]] = None,
    namespaced: Boolean,
    shortNames: Option[List[String]] = None,
    singularName: String,
    group: Option[String] = None,
    storageVersionHash: Option[String] = None,
    kind: String
)

object APIResource {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIResource-Decoder`
      : Decoder[APIResource] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.APIResource-Encoder`
      : Encoder[APIResource] = deriveEncoder
}
