package kubeclient.io.k8s.api.node.v1alpha1

import kubeclient.io.k8s.apimachinery.pkg.api.resource.Quantity

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class Overhead(
    podFixed: Option[Map[String, Quantity]] = None
)

object Overhead {
  implicit val `io.k8s.api.node.v1alpha1.Overhead-Decoder`: Decoder[Overhead] =
    deriveDecoder
  implicit val `io.k8s.api.node.v1alpha1.Overhead-Encoder`: Encoder[Overhead] =
    deriveEncoder
}
