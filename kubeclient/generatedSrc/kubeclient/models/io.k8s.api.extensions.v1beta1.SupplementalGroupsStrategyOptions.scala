package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SupplementalGroupsStrategyOptions(
    ranges: Option[List[IDRange]] = None,
    rule: Option[String] = None
)

object SupplementalGroupsStrategyOptions {
  implicit val `io.k8s.api.extensions.v1beta1.SupplementalGroupsStrategyOptions-Decoder`
      : Decoder[SupplementalGroupsStrategyOptions] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.SupplementalGroupsStrategyOptions-Encoder`
      : Encoder[SupplementalGroupsStrategyOptions] = deriveEncoder
}
