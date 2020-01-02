package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceDefinitionStatus(
    acceptedNames: CustomResourceDefinitionNames,
    conditions: Option[List[CustomResourceDefinitionCondition]] = None,
    storedVersions: List[String]
)

object CustomResourceDefinitionStatus {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceDefinitionStatus-Decoder`
      : Decoder[CustomResourceDefinitionStatus] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceDefinitionStatus-Encoder`
      : Encoder[CustomResourceDefinitionStatus] = deriveEncoder
}
