package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ObjectMeta(
    name: Option[String] = None,
    selfLink: Option[String] = None,
    finalizers: Option[List[String]] = None,
    annotations: Option[Map[String, String]] = None,
    managedFields: Option[List[ManagedFieldsEntry]] = None,
    labels: Option[Map[String, String]] = None,
    uid: Option[String] = None,
    clusterName: Option[String] = None,
    ownerReferences: Option[List[OwnerReference]] = None,
    creationTimestamp: Option[Time] = None,
    generation: Option[Long] = None,
    resourceVersion: Option[String] = None,
    generateName: Option[String] = None,
    deletionGracePeriodSeconds: Option[Long] = None,
    namespace: Option[String] = None,
    deletionTimestamp: Option[Time] = None
)

object ObjectMeta {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta-Decoder`
      : Decoder[ObjectMeta] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta-Encoder`
      : Encoder[ObjectMeta] = deriveEncoder
}
