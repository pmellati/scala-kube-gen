package kubeclient.io.k8s.`kube-aggregator`.pkg.apis.apiregistration.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIServiceStatus(
    conditions: Option[List[APIServiceCondition]] = None
)

object APIServiceStatus {
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1.APIServiceStatus-Decoder`
      : Decoder[APIServiceStatus] = deriveDecoder
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1.APIServiceStatus-Encoder`
      : Encoder[APIServiceStatus] = deriveEncoder
}
