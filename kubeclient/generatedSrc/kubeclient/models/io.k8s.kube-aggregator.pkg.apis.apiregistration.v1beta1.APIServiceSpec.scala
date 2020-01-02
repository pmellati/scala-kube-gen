package kubeclient.io.k8s.`kube-aggregator`.pkg.apis.apiregistration.v1beta1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class APIServiceSpec(
    caBundle: Option[String] = None,
    versionPriority: Int,
    service: ServiceReference,
    version: Option[String] = None,
    groupPriorityMinimum: Int,
    group: Option[String] = None,
    insecureSkipTLSVerify: Option[Boolean] = None
)

object APIServiceSpec {
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1beta1.APIServiceSpec-Decoder`
      : Decoder[APIServiceSpec] = deriveDecoder
  implicit val `io.k8s.kube-aggregator.pkg.apis.apiregistration.v1beta1.APIServiceSpec-Encoder`
      : Encoder[APIServiceSpec] = deriveEncoder
}
