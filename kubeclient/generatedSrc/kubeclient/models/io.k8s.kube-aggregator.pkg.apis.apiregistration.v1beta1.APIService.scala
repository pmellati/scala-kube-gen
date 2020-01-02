package kubeclient.io.k8s.`kube-aggregator`.pkg.apis.apiregistration.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIService(
    status: Option[APIServiceStatus] = None,
    spec: Option[APIServiceSpec] = None,
    metadata: Option[ObjectMeta] = None,
    kind: Option[String] = None,
    apiVersion: Option[String] = None
)

object APIService {
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1beta1.APIService-Decoder`
      : Decoder[APIService] = deriveDecoder
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1beta1.APIService-Encoder`
      : Encoder[APIService] = deriveEncoder
}
