package kubeclient.io.k8s.api.core.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class WindowsSecurityContextOptions(
    gmsaCredentialSpec: Option[String] = None,
    gmsaCredentialSpecName: Option[String] = None,
    runAsUserName: Option[String] = None
)

object WindowsSecurityContextOptions {
  implicit val `io.k8s.api.core.v1.WindowsSecurityContextOptions-Decoder`
      : Decoder[WindowsSecurityContextOptions] = deriveDecoder
  implicit val `io.k8s.api.core.v1.WindowsSecurityContextOptions-Encoder`
      : Encoder[WindowsSecurityContextOptions] = deriveEncoder
}
