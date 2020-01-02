package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceDefinitionCondition(
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object CustomResourceDefinitionCondition {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceDefinitionCondition-Decoder`
      : Decoder[CustomResourceDefinitionCondition] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceDefinitionCondition-Encoder`
      : Encoder[CustomResourceDefinitionCondition] = deriveEncoder
}
