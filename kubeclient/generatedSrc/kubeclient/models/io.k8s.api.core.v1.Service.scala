package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Service(
    status: Option[ServiceStatus] = None,
    spec: Option[ServiceSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object Service {
  implicit val `io.k8s.api.core.v1.Service-Decoder`: Decoder[Service] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Service-Encoder`: Encoder[Service] =
    deriveEncoder
}
