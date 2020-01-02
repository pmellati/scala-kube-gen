package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceSubresources(
    scale: Option[CustomResourceSubresourceScale] = None,
    status: Option[CustomResourceSubresourceStatus] = None
)

object CustomResourceSubresources {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceSubresources-Decoder`
      : Decoder[CustomResourceSubresources] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceSubresources-Encoder`
      : Encoder[CustomResourceSubresources] = deriveEncoder
}
