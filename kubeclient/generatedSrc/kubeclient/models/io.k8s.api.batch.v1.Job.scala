package kubeclient.io.k8s.api.batch.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Job(
    status: Option[JobStatus] = None,
    spec: Option[JobSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object Job {
  implicit val `io.k8s.api.batch.v1.Job-Decoder`: Decoder[Job] = deriveDecoder
  implicit val `io.k8s.api.batch.v1.Job-Encoder`: Encoder[Job] = deriveEncoder
}
