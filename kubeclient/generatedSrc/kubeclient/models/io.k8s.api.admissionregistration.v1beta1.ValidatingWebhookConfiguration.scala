package kubeclient.io.k8s.api.admissionregistration.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ValidatingWebhookConfiguration(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    webhooks: Option[List[ValidatingWebhook]] = None
)

object ValidatingWebhookConfiguration {
  implicit val `io.k8s.api.admissionregistration.v1beta1.ValidatingWebhookConfiguration-Decoder`
      : Decoder[ValidatingWebhookConfiguration] = deriveDecoder
  implicit val `io.k8s.api.admissionregistration.v1beta1.ValidatingWebhookConfiguration-Encoder`
      : Encoder[ValidatingWebhookConfiguration] = deriveEncoder
}
