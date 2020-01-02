package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Pod(
    status: Option[PodStatus] = None,
    spec: Option[PodSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object Pod {
  implicit val `io.k8s.api.core.v1.Pod-Decoder`: Decoder[Pod] = deriveDecoder
  implicit val `io.k8s.api.core.v1.Pod-Encoder`: Encoder[Pod] = deriveEncoder
}
