package kubeclient.io.k8s.api.node.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RuntimeClass(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    spec: RuntimeClassSpec
)

object RuntimeClass {
  implicit val `io.k8s.api.node.v1alpha1.RuntimeClass-Decoder`
      : Decoder[RuntimeClass] = deriveDecoder
  implicit val `io.k8s.api.node.v1alpha1.RuntimeClass-Encoder`
      : Encoder[RuntimeClass] = deriveEncoder
}
