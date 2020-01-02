package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class StatusDetails(
    name: Option[String] = None,
    causes: Option[List[StatusCause]] = None,
    uid: Option[String] = None,
    kind: Option[String] = None,
    retryAfterSeconds: Option[Int] = None,
    group: Option[String] = None
)

object StatusDetails {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.StatusDetails-Decoder`
      : Decoder[StatusDetails] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.StatusDetails-Encoder`
      : Encoder[StatusDetails] = deriveEncoder
}
