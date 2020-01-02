package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ExternalDocumentation(
    description: Option[String] = None,
    url: Option[String] = None
)

object ExternalDocumentation {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.ExternalDocumentation-Decoder`
      : Decoder[ExternalDocumentation] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.ExternalDocumentation-Encoder`
      : Encoder[ExternalDocumentation] = deriveEncoder
}
