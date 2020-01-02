package kubeclient.io.k8s.api.auditregistration.v1alpha1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class WebhookClientConfig(
    caBundle: Option[String] = None,
    service: Option[ServiceReference] = None,
    url: Option[String] = None
)

object WebhookClientConfig {
  implicit val `io.k8s.api.auditregistration.v1alpha1.WebhookClientConfig-Decoder`
      : Decoder[WebhookClientConfig] = deriveDecoder
  implicit val `io.k8s.api.auditregistration.v1alpha1.WebhookClientConfig-Encoder`
      : Encoder[WebhookClientConfig] = deriveEncoder
}
