package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LimitRangeItem(
    max: Option[Map[String, Quantity]] = None,
    default: Option[Map[String, Quantity]] = None,
    `type`: Option[String] = None,
    defaultRequest: Option[Map[String, Quantity]] = None,
    min: Option[Map[String, Quantity]] = None,
    maxLimitRequestRatio: Option[Map[String, Quantity]] = None
)

object LimitRangeItem {
  implicit val `io.k8s.api.core.v1.LimitRangeItem-Decoder`
      : Decoder[LimitRangeItem] = deriveDecoder
  implicit val `io.k8s.api.core.v1.LimitRangeItem-Encoder`
      : Encoder[LimitRangeItem] = deriveEncoder
}
