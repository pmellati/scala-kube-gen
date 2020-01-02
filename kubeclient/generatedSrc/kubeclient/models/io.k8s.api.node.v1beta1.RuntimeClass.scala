package kubeclient.io.k8s.api.node.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RuntimeClass(
    scheduling: Option[Scheduling] = None,
    overhead: Option[Overhead] = None,
    metadata: Option[ObjectMeta] = None,
    handler: String,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object RuntimeClass {
  implicit val `io.k8s.api.node.v1beta1.RuntimeClass-Decoder`
      : Decoder[RuntimeClass] = deriveDecoder
  implicit val `io.k8s.api.node.v1beta1.RuntimeClass-Encoder`
      : Encoder[RuntimeClass] = deriveEncoder
}
