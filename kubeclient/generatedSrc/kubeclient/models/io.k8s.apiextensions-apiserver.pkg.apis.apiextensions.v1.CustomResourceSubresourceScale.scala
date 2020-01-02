package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceSubresourceScale(
    labelSelectorPath: Option[String] = None,
    specReplicasPath: String,
    statusReplicasPath: String
)

object CustomResourceSubresourceScale {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceSubresourceScale-Decoder`
      : Decoder[CustomResourceSubresourceScale] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.CustomResourceSubresourceScale-Encoder`
      : Encoder[CustomResourceSubresourceScale] = deriveEncoder
}
