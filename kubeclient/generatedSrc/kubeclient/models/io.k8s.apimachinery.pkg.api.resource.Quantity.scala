package kubeclient.io.k8s.apimachinery.pkg.api.resource

import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson

/**
  * The OpenAPI specification based on which this file was generated did not contain a definition
  * for this class. Therefore, we simply define the class as a thin wrapper around a json value.
  */
case class Quantity(
    json: Json
)

object Quantity {
  implicit val `io.k8s.apimachinery.pkg.api.resource.Quantity-Decoder`
      : Decoder[Quantity] =
    decodeJson.map(json => Quantity(json))

  implicit val `io.k8s.apimachinery.pkg.api.resource.Quantity-Encoder`
      : Encoder[Quantity] =
    encodeJson.contramap[Quantity](_.json)
}
