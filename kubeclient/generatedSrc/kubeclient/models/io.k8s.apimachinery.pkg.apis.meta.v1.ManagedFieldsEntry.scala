package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ManagedFieldsEntry(
    operation: Option[String] = None,
    manager: Option[String] = None,
    fieldsV1: Option[FieldsV1] = None,
    time: Option[Time] = None,
    apiVersion: Option[String] = None,
    fieldsType: Option[String] = None
)

object ManagedFieldsEntry {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.ManagedFieldsEntry-Decoder`
      : Decoder[ManagedFieldsEntry] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.ManagedFieldsEntry-Encoder`
      : Encoder[ManagedFieldsEntry] = deriveEncoder
}
