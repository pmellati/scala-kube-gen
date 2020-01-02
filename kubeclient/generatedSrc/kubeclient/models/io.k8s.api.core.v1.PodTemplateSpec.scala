package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodTemplateSpec(
    metadata: Option[ObjectMeta] = None,
    spec: Option[PodSpec] = None
)

object PodTemplateSpec {
  implicit val `io.k8s.api.core.v1.PodTemplateSpec-Decoder`
      : Decoder[PodTemplateSpec] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PodTemplateSpec-Encoder`
      : Encoder[PodTemplateSpec] = deriveEncoder
}
