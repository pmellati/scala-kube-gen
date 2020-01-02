package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LabelSelectorRequirement(
    key: String,
    operator: String,
    values: Option[List[String]] = None
)

object LabelSelectorRequirement {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelectorRequirement-Decoder`
      : Decoder[LabelSelectorRequirement] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelectorRequirement-Encoder`
      : Encoder[LabelSelectorRequirement] = deriveEncoder
}
