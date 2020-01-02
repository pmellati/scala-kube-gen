package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HTTPIngressRuleValue(
    paths: List[HTTPIngressPath]
)

object HTTPIngressRuleValue {
  implicit val `io.k8s.api.extensions.v1beta1.HTTPIngressRuleValue-Decoder`
      : Decoder[HTTPIngressRuleValue] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.HTTPIngressRuleValue-Encoder`
      : Encoder[HTTPIngressRuleValue] = deriveEncoder
}
