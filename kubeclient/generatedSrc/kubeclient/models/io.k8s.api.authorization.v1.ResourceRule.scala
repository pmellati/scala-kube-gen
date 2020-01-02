package kubeclient.io.k8s.api.authorization.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class ResourceRule(
    apiGroups: Option[List[String]] = None,
    resourceNames: Option[List[String]] = None,
    resources: Option[List[String]] = None,
    verbs: List[String]
)

object ResourceRule {
  implicit val `io.k8s.api.authorization.v1.ResourceRule-Decoder`
      : Decoder[ResourceRule] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1.ResourceRule-Encoder`
      : Encoder[ResourceRule] = deriveEncoder
}
