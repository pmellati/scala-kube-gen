package kubeclient.io.k8s.api.admissionregistration.v1

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
    admissionReviewVersions: List[String],
    objectSelector: Option[LabelSelector] = None,
    sideEffects: String,
    clientConfig: WebhookClientConfig
)

object ValidatingWebhook {
  implicit val `io.k8s.api.admissionregistration.v1.ValidatingWebhook-Decoder`
      : Decoder[ValidatingWebhook] = deriveDecoder
  implicit val `io.k8s.api.admissionregistration.v1.ValidatingWebhook-Encoder`
      : Encoder[ValidatingWebhook] = deriveEncoder
}
