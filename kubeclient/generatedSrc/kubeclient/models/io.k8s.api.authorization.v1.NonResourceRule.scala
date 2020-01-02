package kubeclient.io.k8s.api.authorization.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class NonResourceRule(
    nonResourceURLs: Option[List[String]] = None,
    verbs: List[String]
)

object NonResourceRule {
  implicit val `io.k8s.api.authorization.v1.NonResourceRule-Decoder`
      : Decoder[NonResourceRule] = deriveDecoder
  implicit val `io.k8s.api.authorization.v1.NonResourceRule-Encoder`
      : Encoder[NonResourceRule] = deriveEncoder
}
