package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LabelSelector(
    matchExpressions: Option[List[LabelSelectorRequirement]] = None,
    matchLabels: Option[Map[String, String]] = None
)

object LabelSelector {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector-Decoder`
      : Decoder[LabelSelector] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector-Encoder`
      : Encoder[LabelSelector] = deriveEncoder
}
