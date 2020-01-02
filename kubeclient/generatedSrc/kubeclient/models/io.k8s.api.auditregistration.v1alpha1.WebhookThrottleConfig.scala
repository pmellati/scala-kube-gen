package kubeclient.io.k8s.api.auditregistration.v1alpha1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class WebhookThrottleConfig(
    burst: Option[Long] = None,
    qps: Option[Long] = None
)

object WebhookThrottleConfig {
  implicit val `io.k8s.api.auditregistration.v1alpha1.WebhookThrottleConfig-Decoder`
      : Decoder[WebhookThrottleConfig] = deriveDecoder
  implicit val `io.k8s.api.auditregistration.v1alpha1.WebhookThrottleConfig-Encoder`
      : Encoder[WebhookThrottleConfig] = deriveEncoder
}
