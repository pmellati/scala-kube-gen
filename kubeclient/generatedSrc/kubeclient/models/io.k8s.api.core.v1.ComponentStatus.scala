package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ComponentStatus(
    apiVersion: Option[String] = None,
    conditions: Option[List[ComponentCondition]] = None,
    kind: Option[String] = None,
    metadata: Option[ObjectMeta] = None
)

object ComponentStatus {
  implicit val `io.k8s.api.core.v1.ComponentStatus-Decoder`
      : Decoder[ComponentStatus] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ComponentStatus-Encoder`
      : Encoder[ComponentStatus] = deriveEncoder
}
