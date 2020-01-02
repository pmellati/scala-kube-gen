package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HostAlias(
    hostnames: Option[List[String]] = None,
    ip: Option[String] = None
)

object HostAlias {
  implicit val `io.k8s.api.core.v1.HostAlias-Decoder`: Decoder[HostAlias] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.HostAlias-Encoder`: Encoder[HostAlias] =
    deriveEncoder
}
