package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeConfigSource(
    configMap: Option[ConfigMapNodeConfigSource] = None
)

object NodeConfigSource {
  implicit val `io.k8s.api.core.v1.NodeConfigSource-Decoder`
      : Decoder[NodeConfigSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeConfigSource-Encoder`
      : Encoder[NodeConfigSource] = deriveEncoder
}
