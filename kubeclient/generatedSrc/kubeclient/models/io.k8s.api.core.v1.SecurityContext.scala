package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class SecurityContext(
    capabilities: Option[Capabilities] = None,
    readOnlyRootFilesystem: Option[Boolean] = None,
    allowPrivilegeEscalation: Option[Boolean] = None,
    procMount: Option[String] = None,
    runAsGroup: Option[Long] = None,
    windowsOptions: Option[WindowsSecurityContextOptions] = None,
    seLinuxOptions: Option[SELinuxOptions] = None,
    runAsNonRoot: Option[Boolean] = None,
    runAsUser: Option[Long] = None,
    privileged: Option[Boolean] = None
)

object SecurityContext {
  implicit val `io.k8s.api.core.v1.SecurityContext-Decoder`
      : Decoder[SecurityContext] = deriveDecoder
  implicit val `io.k8s.api.core.v1.SecurityContext-Encoder`
      : Encoder[SecurityContext] = deriveEncoder
}
