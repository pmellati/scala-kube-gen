package kubeclient.io.k8s.api.batch.v1

import kubeclient.io.k8s.api.core.v1.PodTemplateSpec
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.LabelSelector

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class JobSpec(
    parallelism: Option[Int] = None,
    manualSelector: Option[Boolean] = None,
    template: PodTemplateSpec,
    completions: Option[Int] = None,
    backoffLimit: Option[Int] = None,
    activeDeadlineSeconds: Option[Long] = None,
    ttlSecondsAfterFinished: Option[Int] = None,
    selector: Option[LabelSelector] = None
)

object JobSpec {
  implicit val `io.k8s.api.batch.v1.JobSpec-Decoder`: Decoder[JobSpec] =
    deriveDecoder
  implicit val `io.k8s.api.batch.v1.JobSpec-Encoder`: Encoder[JobSpec] =
    deriveEncoder
}
