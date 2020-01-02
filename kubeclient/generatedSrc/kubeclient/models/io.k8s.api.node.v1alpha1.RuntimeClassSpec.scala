package kubeclient.io.k8s.api.node.v1alpha1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RuntimeClassSpec(
    overhead: Option[Overhead] = None,
    runtimeHandler: String,
    scheduling: Option[Scheduling] = None
)

object RuntimeClassSpec {
  implicit val `io.k8s.api.node.v1alpha1.RuntimeClassSpec-Decoder`
      : Decoder[RuntimeClassSpec] = deriveDecoder
  implicit val `io.k8s.api.node.v1alpha1.RuntimeClassSpec-Encoder`
      : Encoder[RuntimeClassSpec] = deriveEncoder
}
