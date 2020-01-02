package kubeclient.io.k8s.api.batch.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CronJobSpec(
    startingDeadlineSeconds: Option[Long] = None,
    concurrencyPolicy: Option[String] = None,
    failedJobsHistoryLimit: Option[Int] = None,
    successfulJobsHistoryLimit: Option[Int] = None,
    suspend: Option[Boolean] = None,
    schedule: String,
    jobTemplate: JobTemplateSpec
)

object CronJobSpec {
  implicit val `io.k8s.api.batch.v1beta1.CronJobSpec-Decoder`
      : Decoder[CronJobSpec] = deriveDecoder
  implicit val `io.k8s.api.batch.v1beta1.CronJobSpec-Encoder`
      : Encoder[CronJobSpec] = deriveEncoder
}
