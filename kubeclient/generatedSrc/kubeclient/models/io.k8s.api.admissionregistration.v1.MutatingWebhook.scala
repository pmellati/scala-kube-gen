package kubeclient.io.k8s.api.admissionregistration.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class MutatingWebhook(
    timeoutSeconds: Option[Int] = None,
    name: String,
    matchPolicy: Option[String] = None,
    namespaceSelector: Option[LabelSelector] = None,
    failurePolicy: Option[String] = None,
    admissionReviewVersions: List[String],
    objectSelector: Option[LabelSelector] = None,
    sideEffects: String,
    clientConfig: WebhookClientConfig,
    reinvocationPolicy: Option[String] = None,
    rules: Option[List[RuleWithOperations]] = None
)

object MutatingWebhook {
  implicit val `io.k8s.api.admissionregistration.v1.MutatingWebhook-Decoder`
      : Decoder[MutatingWebhook] = deriveDecoder
  implicit val `io.k8s.api.admissionregistration.v1.MutatingWebhook-Encoder`
      : Encoder[MutatingWebhook] = deriveEncoder
}
