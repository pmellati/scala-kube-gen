package kubeclient.io.k8s.api.extensions.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ReplicaSetCondition(
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object ReplicaSetCondition {
  implicit val `io.k8s.api.extensions.v1beta1.ReplicaSetCondition-Decoder`
      : Decoder[ReplicaSetCondition] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.ReplicaSetCondition-Encoder`
      : Encoder[ReplicaSetCondition] = deriveEncoder
}
