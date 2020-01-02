package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceConversion(
    conversionReviewVersions: Option[List[String]] = None,
    strategy: String,
    webhookClientConfig: Option[WebhookClientConfig] = None
)

object CustomResourceConversion {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceConversion-Decoder`
      : Decoder[CustomResourceConversion] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceConversion-Encoder`
      : Encoder[CustomResourceConversion] = deriveEncoder
}
