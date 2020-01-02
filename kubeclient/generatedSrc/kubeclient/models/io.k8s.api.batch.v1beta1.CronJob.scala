package kubeclient.io.k8s.api.batch.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CronJob(
    status: Option[CronJobStatus] = None,
    spec: Option[CronJobSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object CronJob {
  implicit val `io.k8s.api.batch.v1beta1.CronJob-Decoder`: Decoder[CronJob] =
    deriveDecoder
  implicit val `io.k8s.api.batch.v1beta1.CronJob-Encoder`: Encoder[CronJob] =
    deriveEncoder
}
