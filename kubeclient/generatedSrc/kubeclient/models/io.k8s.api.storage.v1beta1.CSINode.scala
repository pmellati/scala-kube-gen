package kubeclient.io.k8s.api.storage.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CSINode(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    spec: CSINodeSpec
)

object CSINode {
  implicit val `io.k8s.api.storage.v1beta1.CSINode-Decoder`: Decoder[CSINode] =
    deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.CSINode-Encoder`: Encoder[CSINode] =
    deriveEncoder
}
