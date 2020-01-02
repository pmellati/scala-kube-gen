package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeAffinity(
    preferredDuringSchedulingIgnoredDuringExecution: Option[
      List[PreferredSchedulingTerm]
    ] = None,
    requiredDuringSchedulingIgnoredDuringExecution: Option[NodeSelector] = None
)

object NodeAffinity {
  implicit val `io.k8s.api.core.v1.NodeAffinity-Decoder`
      : Decoder[NodeAffinity] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeAffinity-Encoder`
      : Encoder[NodeAffinity] = deriveEncoder
}
