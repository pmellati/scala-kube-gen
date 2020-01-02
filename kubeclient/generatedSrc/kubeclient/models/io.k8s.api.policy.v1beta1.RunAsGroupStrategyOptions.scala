package kubeclient.io.k8s.api.policy.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RunAsGroupStrategyOptions(
    ranges: Option[List[IDRange]] = None,
    rule: String
)

object RunAsGroupStrategyOptions {
  implicit val `io.k8s.api.policy.v1beta1.RunAsGroupStrategyOptions-Decoder`
      : Decoder[RunAsGroupStrategyOptions] = deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.RunAsGroupStrategyOptions-Encoder`
      : Encoder[RunAsGroupStrategyOptions] = deriveEncoder
}
