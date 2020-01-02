package kubeclient.io.k8s.api.batch.v1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class JobList(
    apiVersion: Option[String] = None,
    items: List[Job],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object JobList {
  implicit val `io.k8s.api.batch.v1.JobList-Decoder`: Decoder[JobList] =
    deriveDecoder
  implicit val `io.k8s.api.batch.v1.JobList-Encoder`: Encoder[JobList] =
    deriveEncoder
}
