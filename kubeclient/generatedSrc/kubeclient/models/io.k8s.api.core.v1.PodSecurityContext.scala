package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodSecurityContext(
    sysctls: Option[List[Sysctl]] = None,
    windowsOptions: Option[WindowsSecurityContextOptions] = None,
    seLinuxOptions: Option[SELinuxOptions] = None,
    fsGroup: Option[Long] = None,
    supplementalGroups: Option[List[Long]] = None,
    runAsGroup: Option[Long] = None,
    runAsUser: Option[Long] = None,
    runAsNonRoot: Option[Boolean] = None
)

object PodSecurityContext {
  implicit val `io.k8s.api.core.v1.PodSecurityContext-Decoder`
      : Decoder[PodSecurityContext] = deriveDecoder
  implicit val `io.k8s.api.core.v1.PodSecurityContext-Encoder`
      : Encoder[PodSecurityContext] = deriveEncoder
}
