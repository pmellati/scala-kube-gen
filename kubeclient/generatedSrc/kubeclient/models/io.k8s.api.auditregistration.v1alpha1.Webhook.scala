package kubeclient.io.k8s.api.auditregistration.v1alpha1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Webhook(
    clientConfig: WebhookClientConfig,
    throttle: Option[WebhookThrottleConfig] = None
)

object Webhook {
  implicit val `io.k8s.api.auditregistration.v1alpha1.Webhook-Decoder`
      : Decoder[Webhook] = deriveDecoder
  implicit val `io.k8s.api.auditregistration.v1alpha1.Webhook-Encoder`
      : Encoder[Webhook] = deriveEncoder
}
