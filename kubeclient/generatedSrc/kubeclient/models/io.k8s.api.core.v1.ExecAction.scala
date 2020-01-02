package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ExecAction(
    command: Option[List[String]] = None
)

object ExecAction {
  implicit val `io.k8s.api.core.v1.ExecAction-Decoder`: Decoder[ExecAction] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.ExecAction-Encoder`: Encoder[ExecAction] =
    deriveEncoder
}
