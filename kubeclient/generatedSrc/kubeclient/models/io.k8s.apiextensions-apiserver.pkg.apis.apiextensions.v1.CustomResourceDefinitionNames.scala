package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceDefinitionNames(
    plural: String,
    singular: Option[String] = None,
    listKind: Option[String] = None,
    categories: Option[List[String]] = None,
    shortNames: Option[List[String]] = None,
    kind: String
)

object CustomResourceDefinitionNames {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceDefinitionNames-Decoder`
      : Decoder[CustomResourceDefinitionNames] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceDefinitionNames-Encoder`
      : Encoder[CustomResourceDefinitionNames] = deriveEncoder
}
