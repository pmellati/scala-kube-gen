package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StatusCause(
    field: Option[String] = None,
    message: Option[String] = None,
    reason: Option[String] = None
)

object StatusCause {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.StatusCause-Decoder`
      : Decoder[StatusCause] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.StatusCause-Encoder`
      : Encoder[StatusCause] = deriveEncoder
}
