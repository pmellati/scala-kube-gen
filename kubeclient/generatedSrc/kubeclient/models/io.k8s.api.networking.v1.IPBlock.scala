package kubeclient.io.k8s.api.networking.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class IPBlock(
    cidr: String,
    except: Option[List[String]] = None
)

object IPBlock {
  implicit val `io.k8s.api.networking.v1.IPBlock-Decoder`: Decoder[IPBlock] =
    deriveDecoder
  implicit val `io.k8s.api.networking.v1.IPBlock-Encoder`: Encoder[IPBlock] =
    deriveEncoder
}
