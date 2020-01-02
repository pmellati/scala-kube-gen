package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Patch(
    )

object Patch {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.Patch-Decoder`
      : Decoder[Patch] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.Patch-Encoder`
      : Encoder[Patch] = deriveEncoder
}
