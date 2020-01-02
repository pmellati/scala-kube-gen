package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RunAsUserStrategyOptions(
    ranges: Option[List[IDRange]] = None,
    rule: String
)

object RunAsUserStrategyOptions {
  implicit val `io.k8s.api.extensions.v1beta1.RunAsUserStrategyOptions-Decoder`
      : Decoder[RunAsUserStrategyOptions] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.RunAsUserStrategyOptions-Encoder`
      : Encoder[RunAsUserStrategyOptions] = deriveEncoder
}
