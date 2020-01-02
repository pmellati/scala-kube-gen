package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Lifecycle(
    postStart: Option[Handler] = None,
    preStop: Option[Handler] = None
)

object Lifecycle {
  implicit val `io.k8s.api.core.v1.Lifecycle-Decoder`: Decoder[Lifecycle] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Lifecycle-Encoder`: Encoder[Lifecycle] =
    deriveEncoder
}
