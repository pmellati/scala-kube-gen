package kubeclient.io.k8s.api.core.v1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class PodSpec(
    priority: Option[Int] = None,
    affinity: Option[Affinity] = None,
    containers: List[Container],
    runtimeClassName: Option[String] = None,
    hostNetwork: Option[Boolean] = None,
    securityContext: Option[PodSecurityContext] = None,
    hostIPC: Option[Boolean] = None,
    serviceAccount: Option[String] = None,
    schedulerName: Option[String] = None,
    hostname: Option[String] = None,
    imagePullSecrets: Option[List[LocalObjectReference]] = None,
    nodeName: Option[String] = None,
    ephemeralContainers: Option[List[EphemeralContainer]] = None,
    initContainers: Option[List[Container]] = None,
    shareProcessNamespace: Option[Boolean] = None,
    enableServiceLinks: Option[Boolean] = None,
    preemptionPolicy: Option[String] = None,
    dnsPolicy: Option[String] = None,
    volumes: Option[List[Volume]] = None,
    hostAliases: Option[List[HostAlias]] = None,
    subdomain: Option[String] = None,
    topologySpreadConstraints: Option[List[TopologySpreadConstraint]] = None,
    overhead: Option[Map[String, Quantity]] = None,
    tolerations: Option[List[Toleration]] = None,
    automountServiceAccountToken: Option[Boolean] = None,
    nodeSelector: Option[Map[String, String]] = None,
    hostPID: Option[Boolean] = None,
    terminationGracePeriodSeconds: Option[Long] = None,
    dnsConfig: Option[PodDNSConfig] = None,
    priorityClassName: Option[String] = None,
    serviceAccountName: Option[String] = None,
    restartPolicy: Option[String] = None,
    readinessGates: Option[List[PodReadinessGate]] = None,
    activeDeadlineSeconds: Option[Long] = None
)

object PodSpec {
  implicit val `io.k8s.api.core.v1.PodSpec-Decoder`: Decoder[PodSpec] =
    deriveDecoder
  implicit val `io.k8s.api.core.v1.PodSpec-Encoder`: Encoder[PodSpec] =
    deriveEncoder
}
