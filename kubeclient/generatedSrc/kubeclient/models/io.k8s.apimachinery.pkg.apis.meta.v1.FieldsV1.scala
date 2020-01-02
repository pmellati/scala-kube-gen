package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class FieldsV1(
    )

object FieldsV1 {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.FieldsV1-Decoder`
      : Decoder[FieldsV1] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.FieldsV1-Encoder`
      : Encoder[FieldsV1] = deriveEncoder
}
