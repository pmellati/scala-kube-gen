package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Ingress(
    status: Option[IngressStatus] = None,
    spec: Option[IngressSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object Ingress {
  implicit val `io.k8s.api.extensions.v1beta1.Ingress-Decoder`
      : Decoder[Ingress] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.Ingress-Encoder`
      : Encoder[Ingress] = deriveEncoder
}
