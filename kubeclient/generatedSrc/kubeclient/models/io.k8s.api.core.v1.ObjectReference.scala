package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ObjectReference(
    resourceVersion: Option[String] = None,
    uid: Option[String] = None,
    namespace: Option[String] = None,
    fieldPath: Option[String] = None,
    name: Option[String] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object ObjectReference {
  implicit val `io.k8s.api.core.v1.ObjectReference-Decoder`
      : Decoder[ObjectReference] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ObjectReference-Encoder`
      : Encoder[ObjectReference] = deriveEncoder
}
