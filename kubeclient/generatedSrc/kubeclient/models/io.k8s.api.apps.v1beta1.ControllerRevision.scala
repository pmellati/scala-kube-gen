package kubeclient.io.k8s.api.apps.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta
import kubeclient.io.k8s.apimachinery.pkg.runtime.RawExtension

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ControllerRevision(
    data: Option[RawExtension] = None,
    revision: Long,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object ControllerRevision {
  implicit val `io.k8s.api.apps.v1beta1.ControllerRevision-Decoder`
      : Decoder[ControllerRevision] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta1.ControllerRevision-Encoder`
      : Encoder[ControllerRevision] = deriveEncoder
}
