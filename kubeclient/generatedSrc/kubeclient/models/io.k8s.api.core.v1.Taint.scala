package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Taint(
    effect: String,
    key: String,
    timeAdded: Option[Time] = None,
    value: Option[String] = None
)

object Taint {
  implicit val `io.k8s.api.core.v1.Taint-Decoder`: Decoder[Taint] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Taint-Encoder`: Encoder[Taint] =
    deriveEncoder
}
