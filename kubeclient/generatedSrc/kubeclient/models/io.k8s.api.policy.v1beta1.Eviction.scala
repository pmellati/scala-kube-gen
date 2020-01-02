package kubeclient.io.k8s.api.policy.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.DeleteOptions
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Eviction(
    apiVersion: Option[String] = None,
    deleteOptions: Option[DeleteOptions] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None
)

object Eviction {
  implicit val `io.k8s.api.policy.v1beta1.Eviction-Decoder`: Decoder[Eviction] =
    deriveDecoder
  implicit val `io.k8s.api.policy.v1beta1.Eviction-Encoder`: Encoder[Eviction] =
    deriveEncoder
}
