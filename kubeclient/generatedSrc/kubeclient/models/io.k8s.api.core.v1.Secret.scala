package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Secret(
    data: Option[Map[String, String]] = None,
    metadata: Option[ObjectMeta] = None,
    stringData: Option[Map[String, String]] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None,
    `type`: Option[String] = None
)

object Secret {
  implicit val `io.k8s.api.core.v1.Secret-Decoder`: Decoder[Secret] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.Secret-Encoder`: Encoder[Secret] =
    deriveEncoder
}
