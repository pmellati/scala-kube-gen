package kubeclient.io.k8s.api.apps.v1beta2

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RollingUpdateStatefulSetStrategy(
    partition: Option[Int] = None
)

object RollingUpdateStatefulSetStrategy {
  implicit val `io.k8s.api.apps.v1beta2.RollingUpdateStatefulSetStrategy-Decoder`
      : Decoder[RollingUpdateStatefulSetStrategy] = deriveDecoder
  implicit val `io.k8s.api.apps.v1beta2.RollingUpdateStatefulSetStrategy-Encoder`
      : Encoder[RollingUpdateStatefulSetStrategy] = deriveEncoder
}
