package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceDefinitionList(
    apiVersion: Option[String] = None,
    items: List[CustomResourceDefinition],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object CustomResourceDefinitionList {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceDefinitionList-Decoder`
      : Decoder[CustomResourceDefinitionList] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceDefinitionList-Encoder`
      : Encoder[CustomResourceDefinitionList] = deriveEncoder
}
