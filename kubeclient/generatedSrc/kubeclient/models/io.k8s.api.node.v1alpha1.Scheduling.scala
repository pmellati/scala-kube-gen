package kubeclient.io.k8s.api.node.v1alpha1

import kubeclient.io.k8s.api.core.v1.Toleration

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Scheduling(
    nodeSelector: Option[Map[String, String]] = None,
    tolerations: Option[List[Toleration]] = None
)

object Scheduling {
  implicit val `io.k8s.api.node.v1alpha1.Scheduling-Decoder`
      : Decoder[Scheduling] = deriveDecoder
  implicit val `io.k8s.api.node.v1alpha1.Scheduling-Encoder`
      : Encoder[Scheduling] = deriveEncoder
}
