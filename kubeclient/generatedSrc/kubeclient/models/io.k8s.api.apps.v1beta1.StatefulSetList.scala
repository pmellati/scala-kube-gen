package kubeclient.io.k8s.api.apps.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StatefulSetList(
    apiVersion: Option[String] = None,
    items: List[StatefulSet],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object StatefulSetList {
  implicit val `io.k8s.api.apps.v1beta1.StatefulSetList-Decoder`
      : Decoder[StatefulSetList] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta1.StatefulSetList-Encoder`
      : Encoder[StatefulSetList] = deriveEncoder
}
