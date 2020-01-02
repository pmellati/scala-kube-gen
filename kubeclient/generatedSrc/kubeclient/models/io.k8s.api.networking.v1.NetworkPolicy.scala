package kubeclient.io.k8s.api.networking.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NetworkPolicy(
    apiVersion: Option[String] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    spec: Option[NetworkPolicySpec] = None
)

object NetworkPolicy {
  implicit val `io.k8s.api.networking.v1.NetworkPolicy-Decoder`
      : Decoder[NetworkPolicy] = deriveDecoder
  implicit val `io.k8s.api.networking.v1.NetworkPolicy-Encoder`
      : Encoder[NetworkPolicy] = deriveEncoder
}
