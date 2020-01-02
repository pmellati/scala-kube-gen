package kubeclient.io.k8s.`kube-aggregator`.pkg.apis.apiregistration.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.ListMeta

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIServiceList(
    apiVersion: Option[String] = None,
    items: List[APIService],
    kind: Option[String] = None,
    metadata: Option[ListMeta] = None
)

object APIServiceList {
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1beta1.APIServiceList-Decoder`
      : Decoder[APIServiceList] = deriveDecoder
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1beta1.APIServiceList-Encoder`
      : Encoder[APIServiceList] = deriveEncoder
}
