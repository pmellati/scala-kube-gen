package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Status(
    reason: Option[String] = None,
    code: Option[Int] = None,
    status: Option[String] = None,
    message: Option[String] = None,
    details: Option[StatusDetails] = None,
    metadata: Option[ListMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object Status {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.Status-Decoder`
      : Decoder[Status] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.Status-Encoder`
      : Encoder[Status] = deriveEncoder
}
