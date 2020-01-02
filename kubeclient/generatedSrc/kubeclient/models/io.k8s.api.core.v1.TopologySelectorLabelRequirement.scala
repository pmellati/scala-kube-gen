package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TopologySelectorLabelRequirement(
    key: String,
    values: List[String]
)

object TopologySelectorLabelRequirement {
  implicit val `io.k8s.api.core.v1.TopologySelectorLabelRequirement-Decoder`
      : Decoder[TopologySelectorLabelRequirement] = deriveDecoder
  implicit val `io.k8s.api.core.v1.TopologySelectorLabelRequirement-Encoder`
      : Encoder[TopologySelectorLabelRequirement] = deriveEncoder
}
