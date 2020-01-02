package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodAffinityTerm(
    labelSelector: Option[LabelSelector] = None,
    namespaces: Option[List[String]] = None,
    topologyKey: String
)

object PodAffinityTerm {
  implicit val `io.k8s.api.core.v1.PodAffinityTerm-Decoder`
      : Decoder[PodAffinityTerm] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PodAffinityTerm-Encoder`
      : Encoder[PodAffinityTerm] = deriveEncoder
}
