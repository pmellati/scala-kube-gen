package kubeclient.io.k8s.api.coordination.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Lease(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    spec: Option[LeaseSpec] = None
)

object Lease {
  implicit val `io.k8s.api.coordination.v1.Lease-Decoder`: Decoder[Lease] =
    deriveDecoder
  implicit val `io.k8s.api.coordination.v1.Lease-Encoder`: Encoder[Lease] =
    deriveEncoder
}
