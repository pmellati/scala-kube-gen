package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ServiceAccount(
    imagePullSecrets: Option[List[LocalObjectReference]] = None,
    secrets: Option[List[ObjectReference]] = None,
    automountServiceAccountToken: Option[Boolean] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object ServiceAccount {
  implicit val `io.k8s.api.core.v1.ServiceAccount-Decoder`
      : Decoder[ServiceAccount] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ServiceAccount-Encoder`
      : Encoder[ServiceAccount] = deriveEncoder
}
