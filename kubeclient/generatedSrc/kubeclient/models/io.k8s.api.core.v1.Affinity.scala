package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Affinity(
    nodeAffinity: Option[NodeAffinity] = None,
    podAffinity: Option[PodAffinity] = None,
    podAntiAffinity: Option[PodAntiAffinity] = None
)

object Affinity {
  implicit val `io.k8s.api.core.v1.Affinity-Decoder`: Decoder[Affinity] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Affinity-Encoder`: Encoder[Affinity] =
    deriveEncoder
}
