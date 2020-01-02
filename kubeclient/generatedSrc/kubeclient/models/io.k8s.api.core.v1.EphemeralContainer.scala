package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class EphemeralContainer(
    readinessProbe: Option[Probe] = None,
    name: String,
    image: Option[String] = None,
    stdinOnce: Option[Boolean] = None,
    ports: Option[List[ContainerPort]] = None,
    startupProbe: Option[Probe] = None,
    command: Option[List[String]] = None,
    terminationMessagePolicy: Option[String] = None,
    targetContainerName: Option[String] = None,
    imagePullPolicy: Option[String] = None,
    workingDir: Option[String] = None,
    securityContext: Option[SecurityContext] = None,
    livenessProbe: Option[Probe] = None,
    lifecycle: Option[Lifecycle] = None,
    stdin: Option[Boolean] = None,
    terminationMessagePath: Option[String] = None,
    resources: Option[ResourceRequirements] = None,
    envFrom: Option[List[EnvFromSource]] = None,
    tty: Option[Boolean] = None,
    volumeDevices: Option[List[VolumeDevice]] = None,
    args: Option[List[String]] = None,
    volumeMounts: Option[List[VolumeMount]] = None,
    env: Option[List[EnvVar]] = None
)

object EphemeralContainer {
  implicit val `io.k8s.api.core.v1.EphemeralContainer-Decoder`
      : Decoder[EphemeralContainer] = deriveDecoder
  implicit val `io.k8s.api.core.v1.EphemeralContainer-Encoder`
      : Encoder[EphemeralContainer] = deriveEncoder
}
