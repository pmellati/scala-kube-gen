package kubeclient.io.k8s.apimachinery.pkg.runtime

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class RawExtension(
    )

object RawExtension {
  implicit val `io.k8s.apimachinery.pkg.runtime.RawExtension-Decoder`
      : Decoder[RawExtension] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.runtime.RawExtension-Encoder`
      : Encoder[RawExtension] = deriveEncoder
}
