package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.util.intstr.IntOrString

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class HTTPGetAction(
    path: Option[String] = None,
    host: Option[String] = None,
    port: IntOrString,
    scheme: Option[String] = None,
    httpHeaders: Option[List[HTTPHeader]] = None
)

object HTTPGetAction {
  implicit val `io.k8s.api.core.v1.HTTPGetAction-Decoder`
      : Decoder[HTTPGetAction] = deriveDecoder
  implicit val `io.k8s.api.core.v1.HTTPGetAction-Encoder`
      : Encoder[HTTPGetAction] = deriveEncoder
}
