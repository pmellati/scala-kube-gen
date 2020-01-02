package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ObjectFieldSelector(
    apiVersion: Option[String] = None,
    fieldPath: String
)

object ObjectFieldSelector {
  implicit val `io.k8s.api.core.v1.ObjectFieldSelector-Decoder`
      : Decoder[ObjectFieldSelector] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ObjectFieldSelector-Encoder`
      : Encoder[ObjectFieldSelector] = deriveEncoder
}
