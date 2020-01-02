package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceDefinitionSpec(
    conversion: Option[CustomResourceConversion] = None,
    subresources: Option[CustomResourceSubresources] = None,
    version: Option[String] = None,
    group: String,
    validation: Option[CustomResourceValidation] = None,
    versions: Option[List[CustomResourceDefinitionVersion]] = None,
    scope: String,
    names: CustomResourceDefinitionNames,
    preserveUnknownFields: Option[Boolean] = None,
    additionalPrinterColumns: Option[List[CustomResourceColumnDefinition]] =
      None
)

object CustomResourceDefinitionSpec {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceDefinitionSpec-Decoder`
      : Decoder[CustomResourceDefinitionSpec] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceDefinitionSpec-Encoder`
      : Encoder[CustomResourceDefinitionSpec] = deriveEncoder
}
