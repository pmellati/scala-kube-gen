package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class IngressRule(
    host: Option[String] = None,
    http: Option[HTTPIngressRuleValue] = None
)

object IngressRule {
  implicit val `io.k8s.api.extensions.v1beta1.IngressRule-Decoder`
      : Decoder[IngressRule] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.IngressRule-Encoder`
      : Encoder[IngressRule] = deriveEncoder
}
