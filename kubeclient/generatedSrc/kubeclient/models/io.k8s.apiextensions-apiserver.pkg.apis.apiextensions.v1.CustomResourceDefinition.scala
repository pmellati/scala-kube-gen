package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceDefinition(
    status: Option[CustomResourceDefinitionStatus] = None,
    spec: CustomResourceDefinitionSpec,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object CustomResourceDefinition {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceDefinition-Decoder`
      : Decoder[CustomResourceDefinition] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceDefinition-Encoder`
      : Encoder[CustomResourceDefinition] = deriveEncoder
}
