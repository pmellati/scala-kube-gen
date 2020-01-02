package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicationControllerSpec(
    minReadySeconds: Option[Int] = None,
    replicas: Option[Int] = None,
    selector: Option[Map[String, String]] = None,
    template: Option[PodTemplateSpec] = None
)

object ReplicationControllerSpec {
  implicit val `io.k8s.api.core.v1.ReplicationControllerSpec-Decoder`
      : Decoder[ReplicationControllerSpec] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ReplicationControllerSpec-Encoder`
      : Encoder[ReplicationControllerSpec] = deriveEncoder
}
