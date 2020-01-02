package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ServerAddressByClientCIDR(
    clientCIDR: String,
    serverAddress: String
)

object ServerAddressByClientCIDR {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.ServerAddressByClientCIDR-Decoder`
      : Decoder[ServerAddressByClientCIDR] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.ServerAddressByClientCIDR-Encoder`
      : Encoder[ServerAddressByClientCIDR] = deriveEncoder
}
