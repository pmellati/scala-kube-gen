package kubeclient.io.k8s.api.admissionregistration.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class MutatingWebhookConfigurationList(
    apiVersion: Option[String] = None,
    items: List[MutatingWebhookConfiguration],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object MutatingWebhookConfigurationList {
  implicit val `io.k8s.api.admissionregistration.v1beta1.MutatingWebhookConfigurationList-Decoder`
      : Decoder[MutatingWebhookConfigurationList] = deriveDecoder
  implicit val `io.k8s.api.admissionregistration.v1beta1.MutatingWebhookConfigurationList-Encoder`
      : Encoder[MutatingWebhookConfigurationList] = deriveEncoder
}
