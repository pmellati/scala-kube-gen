package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodTemplate(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    template: Option[PodTemplateSpec] = None
)

object PodTemplate {
  implicit val `io.k8s.api.core.v1.PodTemplate-Decoder`: Decoder[PodTemplate] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.PodTemplate-Encoder`: Encoder[PodTemplate] =
    deriveEncoder
}
