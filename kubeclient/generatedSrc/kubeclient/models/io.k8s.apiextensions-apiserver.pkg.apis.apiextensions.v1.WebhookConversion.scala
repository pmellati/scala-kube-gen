package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class WebhookConversion(
    clientConfig: Option[WebhookClientConfig] = None,
    conversionReviewVersions: List[String]
)

object WebhookConversion {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.WebhookConversion-Decoder`
      : Decoder[WebhookConversion] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.WebhookConversion-Encoder`
      : Encoder[WebhookConversion] = deriveEncoder
}
