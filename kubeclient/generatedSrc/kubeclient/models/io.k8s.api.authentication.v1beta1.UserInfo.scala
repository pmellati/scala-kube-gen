package kubeclient.io.k8s.api.authentication.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class UserInfo(
    extra: Option[Map[String, List[String]]] = None,
    groups: Option[List[String]] = None,
    uid: Option[String] = None,
    username: Option[String] = None
)

object UserInfo {
  implicit val `io.k8s.api.authentication.v1beta1.UserInfo-Decoder`
      : Decoder[UserInfo] = deriveDecoder
  implicit val `io.k8s.api.authentication.v1beta1.UserInfo-Encoder`
      : Encoder[UserInfo] = deriveEncoder
}
