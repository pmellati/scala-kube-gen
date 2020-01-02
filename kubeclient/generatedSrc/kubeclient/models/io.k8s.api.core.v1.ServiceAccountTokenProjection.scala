package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ServiceAccountTokenProjection(
    audience: Option[String] = None,
    expirationSeconds: Option[Long] = None,
    path: String
)

object ServiceAccountTokenProjection {
  implicit val `io.k8s.api.core.v1.ServiceAccountTokenProjection-Decoder`
      : Decoder[ServiceAccountTokenProjection] = deriveDecoder
  implicit val `io.k8s.api.core.v1.ServiceAccountTokenProjection-Encoder`
      : Encoder[ServiceAccountTokenProjection] = deriveEncoder
}
