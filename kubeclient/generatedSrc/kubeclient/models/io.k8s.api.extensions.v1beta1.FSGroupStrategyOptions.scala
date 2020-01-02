package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class FSGroupStrategyOptions(
    ranges: Option[List[IDRange]] = None,
    rule: Option[String] = None
)

object FSGroupStrategyOptions {
  implicit val `io.k8s.api.extensions.v1beta1.FSGroupStrategyOptions-Decoder`
      : Decoder[FSGroupStrategyOptions] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.FSGroupStrategyOptions-Encoder`
      : Encoder[FSGroupStrategyOptions] = deriveEncoder
}
