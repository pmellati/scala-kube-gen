package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class GitRepoVolumeSource(
    directory: Option[String] = None,
    repository: String,
    revision: Option[String] = None
)

object GitRepoVolumeSource {
  implicit val `io.k8s.api.core.v1.GitRepoVolumeSource-Decoder`
      : Decoder[GitRepoVolumeSource] = deriveDecoder
  implicit val `io.k8s.api.core.v1.GitRepoVolumeSource-Encoder`
      : Encoder[GitRepoVolumeSource] = deriveEncoder
}
