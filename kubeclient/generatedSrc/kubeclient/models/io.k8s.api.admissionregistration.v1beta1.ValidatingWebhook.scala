package kubeclient.io.k8s.api.admissionregistration.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ValidatingWebhook(
    timeoutSeconds: Option[Int] = None,
    name: String,
    matchPolicy: Option[String] = None,
    namespaceSelector: Option[LabelSelector] = None,
    failurePolicy: Option[String] = None,
    rules: Option[List[RuleWithOperations]] = None,
    admissionReviewVersions: Option[List[String]] = None,
    objectSelector: Option[LabelSelector] = None,
    sideEffects: Option[String] = None,
    clientConfig: WebhookClientConfig
)

object ValidatingWebhook {
  implicit val `io.k8s.api.admissionregistration.v1beta1.ValidatingWebhook-Decoder`
      : Decoder[ValidatingWebhook] = deriveDecoder
  implicit val `io.k8s.api.admissionregistration.v1beta1.ValidatingWebhook-Encoder`
      : Encoder[ValidatingWebhook] = deriveEncoder
}
