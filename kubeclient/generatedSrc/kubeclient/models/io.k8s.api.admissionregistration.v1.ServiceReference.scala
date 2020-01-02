package kubeclient.io.k8s.api.admissionregistration.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ServiceReference(
    name: String,
    namespace: String,
    path: Option[String] = None,
    port: Option[Int] = None
)

object ServiceReference {
  implicit val `io.k8s.api.admissionregistration.v1.ServiceReference-Decoder`
      : Decoder[ServiceReference] = deriveDecoder
  implicit val `io.k8s.api.admissionregistration.v1.ServiceReference-Encoder`
      : Encoder[ServiceReference] = deriveEncoder
}
