package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LimitRange(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    spec: Option[LimitRangeSpec] = None
)

object LimitRange {
  implicit val `io.k8s.api.core.v1.LimitRange-Decoder`: Decoder[LimitRange] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.LimitRange-Encoder`: Encoder[LimitRange] =
    deriveEncoder
}
