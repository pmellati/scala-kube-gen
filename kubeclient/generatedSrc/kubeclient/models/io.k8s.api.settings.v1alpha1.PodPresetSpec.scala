package kubeclient.io.k8s.api.settings.v1alpha1

import kubeclient.io.k8s.api.core.v1.EnvFromSource
import kubeclient.io.k8s.api.core.v1.EnvVar
import kubeclient.io.k8s.api.core.v1.Volume
import kubeclient.io.k8s.api.core.v1.VolumeMount
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodPresetSpec(
    volumes: Option[List[Volume]] = None,
    envFrom: Option[List[EnvFromSource]] = None,
    selector: Option[LabelSelector] = None,
    volumeMounts: Option[List[VolumeMount]] = None,
    env: Option[List[EnvVar]] = None
)

object PodPresetSpec {
  implicit val `io.k8s.api.settings.v1alpha1.PodPresetSpec-Decoder`
      : Decoder[PodPresetSpec] = deriveDecoder
  implicit val `io.k8s.api.settings.v1alpha1.PodPresetSpec-Encoder`
      : Encoder[PodPresetSpec] = deriveEncoder
}
