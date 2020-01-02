package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NodeSystemInfo(
    systemUUID: String,
    osImage: String,
    machineID: String,
    bootID: String,
    architecture: String,
    kubeProxyVersion: String,
    containerRuntimeVersion: String,
    kernelVersion: String,
    kubeletVersion: String,
    operatingSystem: String
)

object NodeSystemInfo {
  implicit val `io.k8s.api.core.v1.NodeSystemInfo-Decoder`
      : Decoder[NodeSystemInfo] = deriveDecoder
  implicit val `io.k8s.api.core.v1.NodeSystemInfo-Encoder`
      : Encoder[NodeSystemInfo] = deriveEncoder
}
