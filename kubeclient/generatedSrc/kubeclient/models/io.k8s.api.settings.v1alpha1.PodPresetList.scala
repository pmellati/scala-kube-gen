package kubeclient.io.k8s.api.settings.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodPresetList(
    apiVersion: Option[String] = None,
    items: List[PodPreset],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object PodPresetList {
  implicit val `io.k8s.api.settings.v1alpha1.PodPresetList-Decoder`
      : Decoder[PodPresetList] = deriveDecoder
  implicit val `io.k8s.api.settings.v1alpha1.PodPresetList-Encoder`
      : Encoder[PodPresetList] = deriveEncoder
}
