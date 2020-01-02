package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CSIPersistentVolumeSource(
    nodeStageSecretRef: Option[SecretReference] = None,
    readOnly: Option[Boolean] = None,
    fsType: Option[String] = None,
    volumeHandle: String,
    volumeAttributes: Option[Map[String, String]] = None,
    controllerExpandSecretRef: Option[SecretReference] = None,
    nodePublishSecretRef: Option[SecretReference] = None,
    driver: String,
    controllerPublishSecretRef: Option[SecretReference] = None
)

object CSIPersistentVolumeSource {
  implicit val `io.k8s.api.core.v1.CSIPersistentVolumeSource-Decoder`
      : Decoder[CSIPersistentVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.CSIPersistentVolumeSource-Encoder`
      : Encoder[CSIPersistentVolumeSource] = deriveEncoder
}
