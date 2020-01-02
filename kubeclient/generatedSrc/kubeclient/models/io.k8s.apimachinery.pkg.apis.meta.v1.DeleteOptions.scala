package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class DeleteOptions(
    orphanDependents: Option[Boolean] = None,
    preconditions: Option[Preconditions] = None,
    dryRun: Option[List[String]] = None,
    propagationPolicy: Option[String] = None,
    gracePeriodSeconds: Option[Long] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object DeleteOptions {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.DeleteOptions-Decoder`
      : Decoder[DeleteOptions] = deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.DeleteOptions-Encoder`
      : Encoder[DeleteOptions] = deriveEncoder
}
