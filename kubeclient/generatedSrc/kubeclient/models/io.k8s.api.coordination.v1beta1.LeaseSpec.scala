package kubeclient.io.k8s.api.coordination.v1beta1

import kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1.MicroTime

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class LeaseSpec(
    leaseTransitions: Option[Int] = None,
    holderIdentity: Option[String] = None,
    leaseDurationSeconds: Option[Int] = None,
    renewTime: Option[MicroTime] = None,
    acquireTime: Option[MicroTime] = None
)

object LeaseSpec {
  implicit val `io.k8s.api.coordination.v1beta1.LeaseSpec-Decoder`
      : Decoder[LeaseSpec] = deriveDecoder
  implicit val `io.k8s.api.coordination.v1beta1.LeaseSpec-Encoder`
      : Encoder[LeaseSpec] = deriveEncoder
}
