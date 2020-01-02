package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EnvVarSource(
    configMapKeyRef: Option[ConfigMapKeySelector] = None,
    fieldRef: Option[ObjectFieldSelector] = None,
    resourceFieldRef: Option[ResourceFieldSelector] = None,
    secretKeyRef: Option[SecretKeySelector] = None
)

object EnvVarSource {
  implicit val `io.k8s.api.core.v1.EnvVarSource-Decoder`
      : Decoder[EnvVarSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.EnvVarSource-Encoder`
      : Encoder[EnvVarSource] = deriveEncoder
}
