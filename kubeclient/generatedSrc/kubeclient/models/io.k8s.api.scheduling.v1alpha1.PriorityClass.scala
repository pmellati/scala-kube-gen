package kubeclient.io.k8s.api.scheduling.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PriorityClass(
    description: Option[String] = None,
    globalDefault: Option[Boolean] = None,
    preemptionPolicy: Option[String] = None,
    metadata: Option[ObjectMeta] = None,
    value: Int,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object PriorityClass {
  implicit val `io.k8s.api.scheduling.v1alpha1.PriorityClass-Decoder`
      : Decoder[PriorityClass] = deriveDecoder
  implicit val `io.k8s.api.scheduling.v1alpha1.PriorityClass-Encoder`
      : Encoder[PriorityClass] = deriveEncoder
}
