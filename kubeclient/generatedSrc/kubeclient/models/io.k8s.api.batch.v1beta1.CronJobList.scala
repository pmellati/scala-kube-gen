package kubeclient.io.k8s.api.batch.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CronJobList(
    apiVersion: Option[String] = None,
    items: List[CronJob],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object CronJobList {
  implicit val `io.k8s.api.batch.v1beta1.CronJobList-Decoder`
      : Decoder[CronJobList] = deriveDecoder
  implicit val `io.k8s.api.batch.v1beta1.CronJobList-Encoder`
      : Encoder[CronJobList] = deriveEncoder
}
