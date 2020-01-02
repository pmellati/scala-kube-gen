package kubeclient.io.k8s.api.authorization.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceAttributes(
    name: Option[String] = None,
    version: Option[String] = None,
    resource: Option[String] = None,
    verb: Option[String] = None,
    group: Option[String] = None,
    subresource: Option[String] = None,
    namespace: Option[String] = None
)

object ResourceAttributes {
  implicit val `io.k8s.api.authorization.v1.ResourceAttributes-Decoder`
      : Decoder[ResourceAttributes] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1.ResourceAttributes-Encoder`
      : Encoder[ResourceAttributes] = deriveEncoder
}
