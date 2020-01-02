package kubeclient.io.k8s.api.apps.v1beta2

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DaemonSet(
    status: Option[DaemonSetStatus] = None,
    spec: Option[DaemonSetSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object DaemonSet {
  implicit val `io.k8s.api.apps.v1beta2.DaemonSet-Decoder`: Decoder[DaemonSet] =
    deriveDecoder
  implicit val `io.k8s.api.apps.v1beta2.DaemonSet-Encoder`: Encoder[DaemonSet] =
    deriveEncoder
}
