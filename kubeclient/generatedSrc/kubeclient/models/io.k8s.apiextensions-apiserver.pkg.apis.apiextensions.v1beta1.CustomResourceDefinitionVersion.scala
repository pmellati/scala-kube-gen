package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceDefinitionVersion(
    name: String,
    served: Boolean,
    subresources: Option[CustomResourceSubresources] = None,
    schema: Option[CustomResourceValidation] = None,
    additionalPrinterColumns: Option[List[CustomResourceColumnDefinition]] =
      None,
    storage: Boolean
)

object CustomResourceDefinitionVersion {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceDefinitionVersion-Decoder`
      : Decoder[CustomResourceDefinitionVersion] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceDefinitionVersion-Encoder`
      : Encoder[CustomResourceDefinitionVersion] = deriveEncoder
}
