package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceDefinitionSpec(
    conversion: Option[CustomResourceConversion] = None,
    preserveUnknownFields: Option[Boolean] = None,
    group: String,
    versions: List[CustomResourceDefinitionVersion],
    scope: String,
    names: CustomResourceDefinitionNames
)

object CustomResourceDefinitionSpec {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceDefinitionSpec-Decoder`
      : Decoder[CustomResourceDefinitionSpec] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceDefinitionSpec-Encoder`
      : Encoder[CustomResourceDefinitionSpec] = deriveEncoder
}
