package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RuntimeClassStrategyOptions(
    allowedRuntimeClassNames: List[String],
    defaultRuntimeClassName: Option[String] = None
)

object RuntimeClassStrategyOptions {
  implicit val `io.k8s.api.extensions.v1beta1.RuntimeClassStrategyOptions-Decoder`
      : Decoder[RuntimeClassStrategyOptions] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.RuntimeClassStrategyOptions-Encoder`
      : Encoder[RuntimeClassStrategyOptions] = deriveEncoder
}
