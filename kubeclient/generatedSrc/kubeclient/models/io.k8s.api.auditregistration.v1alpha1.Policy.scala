package kubeclient.io.k8s.api.auditregistration.v1alpha1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Policy(
    level: String,
    stages: Option[List[String]] = None
)

object Policy {
  implicit val `io.k8s.api.auditregistration.v1alpha1.Policy-Decoder`
      : Decoder[Policy] = deriveDecoder
  implicit val `io.k8s.api.auditregistration.v1alpha1.Policy-Encoder`
      : Encoder[Policy] = deriveEncoder
}
