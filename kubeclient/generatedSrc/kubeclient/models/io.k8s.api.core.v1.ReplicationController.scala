package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicationController(
    status: Option[ReplicationControllerStatus] = None,
    spec: Option[ReplicationControllerSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object ReplicationController {
  implicit val `io.k8s.api.core.v1.ReplicationController-Decoder`
      : Decoder[ReplicationController] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ReplicationController-Encoder`
      : Encoder[ReplicationController] = deriveEncoder
}
