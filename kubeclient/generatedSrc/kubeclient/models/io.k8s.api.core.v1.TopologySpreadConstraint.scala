package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class TopologySpreadConstraint(
    labelSelector: Option[LabelSelector] = None,
    maxSkew: Int,
    topologyKey: String,
    whenUnsatisfiable: String
)

object TopologySpreadConstraint {
  implicit val `io.k8s.api.core.v1.TopologySpreadConstraint-Decoder`
      : Decoder[TopologySpreadConstraint] = deriveDecoder
  implicit val `io.k8s.api.core.v1.TopologySpreadConstraint-Encoder`
      : Encoder[TopologySpreadConstraint] = deriveEncoder
}
