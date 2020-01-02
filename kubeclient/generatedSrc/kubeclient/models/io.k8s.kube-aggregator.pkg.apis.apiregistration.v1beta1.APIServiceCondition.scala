package kubeclient.io.k8s.`kube-aggregator`.pkg.apis.apiregistration.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIServiceCondition(
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object APIServiceCondition {
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1beta1.APIServiceCondition-Decoder`
      : Decoder[APIServiceCondition] = deriveDecoder
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1beta1.APIServiceCondition-Encoder`
      : Encoder[APIServiceCondition] = deriveEncoder
}
