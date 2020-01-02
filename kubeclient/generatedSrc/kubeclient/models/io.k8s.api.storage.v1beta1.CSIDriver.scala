package kubeclient.io.k8s.api.storage.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CSIDriver(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    spec: CSIDriverSpec
)

object CSIDriver {
  implicit val `io.k8s.api.storage.v1beta1.CSIDriver-Decoder`
      : Decoder[CSIDriver] = deriveDecoder
  implicit val `io.k8s.api.storage.v1beta1.CSIDriver-Encoder`
      : Encoder[CSIDriver] = deriveEncoder
}
