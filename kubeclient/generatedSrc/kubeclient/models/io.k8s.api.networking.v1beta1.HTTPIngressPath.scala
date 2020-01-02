package kubeclient.io.k8s.api.networking.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HTTPIngressPath(
    backend: IngressBackend,
    path: Option[String] = None
)

object HTTPIngressPath {
  implicit val `io.k8s.api.networking.v1beta1.HTTPIngressPath-Decoder`
      : Decoder[HTTPIngressPath] = deriveDecoder
  implicit val `io.k8s.api.networking.v1beta1.HTTPIngressPath-Encoder`
      : Encoder[HTTPIngressPath] = deriveEncoder
}
