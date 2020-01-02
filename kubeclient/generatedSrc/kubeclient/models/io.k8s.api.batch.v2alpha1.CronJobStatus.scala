package kubeclient.io.k8s.api.batch.v2alpha1

import kubeclient.io.k8s.api.core.v1.ObjectReference
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class CronJobStatus(
    active: Option[List[ObjectReference]] = None,
    lastScheduleTime: Option[Time] = None
)

object CronJobStatus {
  implicit val `io.k8s.api.batch.v2alpha1.CronJobStatus-Decoder`
      : Decoder[CronJobStatus] = deriveDecoder
  implicit val `io.k8s.api.batch.v2alpha1.CronJobStatus-Encoder`
      : Encoder[CronJobStatus] = deriveEncoder
}
