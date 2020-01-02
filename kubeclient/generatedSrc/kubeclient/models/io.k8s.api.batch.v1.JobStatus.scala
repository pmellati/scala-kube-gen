package kubeclient.io.k8s.api.batch.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.Time

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class JobStatus(
    conditions: Option[List[JobCondition]] = None,
    startTime: Option[Time] = None,
    failed: Option[Int] = None,
    succeeded: Option[Int] = None,
    completionTime: Option[Time] = None,
    active: Option[Int] = None
)

object JobStatus {
  implicit val `io.k8s.api.batch.v1.JobStatus-Decoder`: Decoder[JobStatus] =
    deriveDecoder
  implicit val `io.k8s.api.batch.v1.JobStatus-Encoder`: Encoder[JobStatus] =
    deriveEncoder
}
