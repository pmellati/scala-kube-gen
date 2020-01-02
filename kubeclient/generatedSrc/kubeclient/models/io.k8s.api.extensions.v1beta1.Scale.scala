package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Scale(
    status: Option[ScaleStatus] = None,
    spec: Option[ScaleSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object Scale {
  implicit val `io.k8s.api.extensions.v1beta1.Scale-Decoder`: Decoder[Scale] =
    deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.Scale-Encoder`: Encoder[Scale] =
    deriveEncoder
}
