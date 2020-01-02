package kubeclient.io.k8s.api.settings.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodPreset(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    spec: Option[PodPresetSpec] = None
)

object PodPreset {
  implicit val `io.k8s.api.settings.v1alpha1.PodPreset-Decoder`
      : Decoder[PodPreset] = deriveDecoder
  implicit val `io.k8s.api.settings.v1alpha1.PodPreset-Encoder`
      : Encoder[PodPreset] = deriveEncoder
}
