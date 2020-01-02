package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CustomResourceSubresourceStatus(
    )

object CustomResourceSubresourceStatus {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceSubresourceStatus-Decoder`
      : Decoder[CustomResourceSubresourceStatus] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.CustomResourceSubresourceStatus-Encoder`
      : Encoder[CustomResourceSubresourceStatus] = deriveEncoder
}
