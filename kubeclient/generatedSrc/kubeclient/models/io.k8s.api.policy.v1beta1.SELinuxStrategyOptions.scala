package kubeclient.io.k8s.api.policy.v1beta1

import kubeclient.io.k8s.api.core.v1.SELinuxOptions

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SELinuxStrategyOptions(
    rule: String,
    seLinuxOptions: Option[SELinuxOptions] = None
)

object SELinuxStrategyOptions {
  implicit val `io.k8s.api.policy.v1beta1.SELinuxStrategyOptions-Decoder`
      : Decoder[SELinuxStrategyOptions] = deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.SELinuxStrategyOptions-Encoder`
      : Encoder[SELinuxStrategyOptions] = deriveEncoder
}
