package kubeclient.io.k8s.apimachinery.pkg.version

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Info(
    compiler: String,
    gitVersion: String,
    gitTreeState: String,
    major: String,
    minor: String,
    gitCommit: String,
    goVersion: String,
    platform: String,
    buildDate: String
)

object Info {
  implicit val `io.k8s.apimachinery.pkg.version.Info-Decoder`: Decoder[Info] =
    deriveDecoder
  implicit val `io.k8s.apimachinery.pkg.version.Info-Encoder`: Encoder[Info] =
    deriveEncoder
}
