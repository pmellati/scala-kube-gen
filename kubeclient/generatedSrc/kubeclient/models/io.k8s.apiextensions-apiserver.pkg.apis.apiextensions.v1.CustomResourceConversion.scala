package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceConversion(
    strategy: String,
    webhook: Option[WebhookConversion] = None
)

object CustomResourceConversion {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceConversion-Decoder`
      : Decoder[CustomResourceConversion] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceConversion-Encoder`
      : Encoder[CustomResourceConversion] = deriveEncoder
}
