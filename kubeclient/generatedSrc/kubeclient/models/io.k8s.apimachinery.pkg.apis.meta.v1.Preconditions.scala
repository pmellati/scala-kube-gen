package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Preconditions(
    resourceVersion: Option[String] = None,
    uid: Option[String] = None
)

object Preconditions {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.Preconditions-Decoder`
      : Decoder[Preconditions] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.Preconditions-Encoder`
      : Encoder[Preconditions] = deriveEncoder
}
