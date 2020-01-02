package kubeclient.io.k8s.api.admissionregistration.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class MutatingWebhookConfiguration(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    webhooks: Option[List[MutatingWebhook]] = None
)

object MutatingWebhookConfiguration {
  implicit val `io.k8s.api.admissionregistration.v1.MutatingWebhookConfiguration-Decoder`
      : Decoder[MutatingWebhookConfiguration] = deriveDecoder
  implicit val `io.k8s.api.admissionregistration.v1.MutatingWebhookConfiguration-Encoder`
      : Encoder[MutatingWebhookConfiguration] = deriveEncoder
}
