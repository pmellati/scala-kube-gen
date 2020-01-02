package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class AllowedHostPath(
    pathPrefix: Option[String] = None,
    readOnly: Option[Boolean] = None
)

object AllowedHostPath {
  implicit val `io.k8s.api.extensions.v1beta1.AllowedHostPath-Decoder`
      : Decoder[AllowedHostPath] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.AllowedHostPath-Encoder`
      : Encoder[AllowedHostPath] = deriveEncoder
}
