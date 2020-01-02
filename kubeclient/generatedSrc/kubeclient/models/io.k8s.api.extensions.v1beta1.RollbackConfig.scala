package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RollbackConfig(
    revision: Option[Long] = None
)

object RollbackConfig {
  implicit val `io.k8s.api.extensions.v1beta1.RollbackConfig-Decoder`
      : Decoder[RollbackConfig] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.RollbackConfig-Encoder`
      : Encoder[RollbackConfig] = deriveEncoder
}
