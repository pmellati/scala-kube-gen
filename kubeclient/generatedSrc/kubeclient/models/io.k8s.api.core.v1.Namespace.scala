package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Namespace(
    status: Option[NamespaceStatus] = None,
    spec: Option[NamespaceSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object Namespace {
  implicit val `io.k8s.api.core.v1.Namespace-Decoder`: Decoder[Namespace] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Namespace-Encoder`: Encoder[Namespace] =
    deriveEncoder
}
