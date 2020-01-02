package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceValidation(
    openAPIV3Schema: Option[JSONSchemaProps] = None
)

object CustomResourceValidation {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceValidation-Decoder`
      : Decoder[CustomResourceValidation] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceValidation-Encoder`
      : Encoder[CustomResourceValidation] = deriveEncoder
}
