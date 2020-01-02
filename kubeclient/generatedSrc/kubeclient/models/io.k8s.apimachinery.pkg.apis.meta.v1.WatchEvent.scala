package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import kubeclient.io.k8s.apimachinery.pkg.runtime.RawExtension

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class WatchEvent(
    `object`: RawExtension,
    `type`: String
)

object WatchEvent {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.WatchEvent-Decoder`
      : Decoder[WatchEvent] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.WatchEvent-Encoder`
      : Encoder[WatchEvent] = deriveEncoder
}
