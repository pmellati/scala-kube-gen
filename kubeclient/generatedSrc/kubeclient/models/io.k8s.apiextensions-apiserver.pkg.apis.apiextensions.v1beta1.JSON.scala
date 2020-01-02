package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1beta1

import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson

/**
  * The OpenAPI specification based on which this file was generated did not contain a definition
  * for this class. Therefore, we simply define the class as a thin wrapper around a json value.
  */
case class JSON(
    json: Json
)

object JSON {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.JSON-Decoder`
      : Decoder[JSON] =
    decodeJson.map(json => JSON(json))

  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.JSON-Encoder`
      : Encoder[JSON] =
    encodeJson.contramap[JSON](_.json)
}
