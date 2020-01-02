package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class IngressTLS(
    hosts: Option[List[String]] = None,
    secretName: Option[String] = None
)

object IngressTLS {
  implicit val `io.k8s.api.extensions.v1beta1.IngressTLS-Decoder`
      : Decoder[IngressTLS] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.IngressTLS-Encoder`
      : Encoder[IngressTLS] = deriveEncoder
}
