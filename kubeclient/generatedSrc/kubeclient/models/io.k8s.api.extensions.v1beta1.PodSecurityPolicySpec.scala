package kubeclient.io.k8s.api.extensions.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodSecurityPolicySpec(
    seLinux: SELinuxStrategyOptions,
    readOnlyRootFilesystem: Option[Boolean] = None,
    allowedCSIDrivers: Option[List[AllowedCSIDriver]] = None,
    allowPrivilegeEscalation: Option[Boolean] = None,
    volumes: Option[List[String]] = None,
    allowedProcMountTypes: Option[List[String]] = None,
    allowedUnsafeSysctls: Option[List[String]] = None,
    hostPID: Option[Boolean] = None,
    defaultAllowPrivilegeEscalation: Option[Boolean] = None,
    requiredDropCapabilities: Option[List[String]] = None,
    allowedFlexVolumes: Option[List[AllowedFlexVolume]] = None,
    hostNetwork: Option[Boolean] = None,
    hostPorts: Option[List[HostPortRange]] = None,
    allowedHostPaths: Option[List[AllowedHostPath]] = None,
    hostIPC: Option[Boolean] = None,
    forbiddenSysctls: Option[List[String]] = None,
    runtimeClass: Option[RuntimeClassStrategyOptions] = None,
    fsGroup: FSGroupStrategyOptions,
    supplementalGroups: SupplementalGroupsStrategyOptions,
    runAsGroup: Option[RunAsGroupStrategyOptions] = None,
    defaultAddCapabilities: Option[List[String]] = None,
    allowedCapabilities: Option[List[String]] = None,
    runAsUser: RunAsUserStrategyOptions,
    privileged: Option[Boolean] = None
)

object PodSecurityPolicySpec {
  implicit val `io.k8s.api.extensions.v1beta1.PodSecurityPolicySpec-Decoder`
      : Decoder[PodSecurityPolicySpec] = deriveDecoder
  implicit val `io.k8s.api.extensions.v1beta1.PodSecurityPolicySpec-Encoder`
      : Encoder[PodSecurityPolicySpec] = deriveEncoder
}
