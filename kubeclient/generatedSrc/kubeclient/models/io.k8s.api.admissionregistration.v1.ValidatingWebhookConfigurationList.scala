package kubeclient.io.k8s.api.admissionregistration.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ValidatingWebhookConfigurationList(
    apiVersion: Option[String] = None,
    items: List[ValidatingWebhookConfiguration],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object ValidatingWebhookConfigurationList {
  implicit val `io.k8s.api.admissionregistration.v1.ValidatingWebhookConfigurationList-Decoder`
      : Decoder[ValidatingWebhookConfigurationList] = deriveDecoder
  implicit val `io.k8s.api.admissionregistration.v1.ValidatingWebhookConfigurationList-Encoder`
      : Encoder[ValidatingWebhookConfigurationList] = deriveEncoder
}
