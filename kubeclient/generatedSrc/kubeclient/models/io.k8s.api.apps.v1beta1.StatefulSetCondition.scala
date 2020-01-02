package kubeclient.io.k8s.api.apps.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StatefulSetCondition(
    reason: Option[String] = None,
    message: Option[String] = None,
    `type`: String,
    status: String,
    lastTransitionTime: Option[Time] = None
)

object StatefulSetCondition {
  implicit val `io.k8s.api.apps.v1beta1.StatefulSetCondition-Decoder`
      : Decoder[StatefulSetCondition] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta1.StatefulSetCondition-Encoder`
      : Encoder[StatefulSetCondition] = deriveEncoder
}
