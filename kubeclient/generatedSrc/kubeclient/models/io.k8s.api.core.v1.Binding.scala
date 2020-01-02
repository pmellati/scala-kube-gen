package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Binding(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    target: ObjectReference
)

object Binding {
  implicit val `io.k8s.api.core.v1.Binding-Decoder`: Decoder[Binding] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Binding-Encoder`: Encoder[Binding] =
    deriveEncoder
}
