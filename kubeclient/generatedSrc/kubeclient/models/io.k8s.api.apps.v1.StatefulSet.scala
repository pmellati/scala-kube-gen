package kubeclient.io.k8s.api.apps.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StatefulSet(
    status: Option[StatefulSetStatus] = None,
    spec: Option[StatefulSetSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object StatefulSet {
  implicit val `io.k8s.api.apps.v1.StatefulSet-Decoder`: Decoder[StatefulSet] =
    deriveDecoder
  implicit val `io.k8s.api.apps.v1.StatefulSet-Encoder`: Encoder[StatefulSet] =
    deriveEncoder
}
