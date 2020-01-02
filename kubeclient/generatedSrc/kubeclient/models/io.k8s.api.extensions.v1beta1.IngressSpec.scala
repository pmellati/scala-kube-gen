package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class IngressSpec(
    backend: Option[IngressBackend] = None,
    rules: Option[List[IngressRule]] = None,
    tls: Option[List[IngressTLS]] = None
)

object IngressSpec {
  implicit val `io.k8s.api.extensions.v1beta1.IngressSpec-Decoder`
      : Decoder[IngressSpec] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.IngressSpec-Encoder`
      : Encoder[IngressSpec] = deriveEncoder
}
