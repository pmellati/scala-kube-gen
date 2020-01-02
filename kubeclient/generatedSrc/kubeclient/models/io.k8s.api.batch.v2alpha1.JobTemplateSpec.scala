package kubeclient.io.k8s.api.batch.v2alpha1

import kubeclient.io.k8s.api.batch.v1.JobSpec
import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class JobTemplateSpec(
    metadata: Option[ObjectMeta] = None,
    spec: Option[JobSpec] = None
)

object JobTemplateSpec {
  implicit val `io.k8s.api.batch.v2alpha1.JobTemplateSpec-Decoder`
      : Decoder[JobTemplateSpec] = deriveDecoder
  implicit val `io.k8s.api.batch.v2alpha1.JobTemplateSpec-Encoder`
      : Encoder[JobTemplateSpec] = deriveEncoder
}
