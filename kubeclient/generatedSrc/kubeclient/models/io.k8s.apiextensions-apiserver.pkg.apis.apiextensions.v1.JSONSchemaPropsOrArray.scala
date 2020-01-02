package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson

/**
  * The OpenAPI specification based on which this file was generated did not contain a definition
  * for this class. Therefore, we simply define the class as a thin wrapper around a json value.
  */
case class JSONSchemaPropsOrArray(
    json: Json
)

object JSONSchemaPropsOrArray {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.JSONSchemaPropsOrArray-Decoder`
      : Decoder[JSONSchemaPropsOrArray] =
    decodeJson.map(json => JSONSchemaPropsOrArray(json))

  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.JSONSchemaPropsOrArray-Encoder`
      : Encoder[JSONSchemaPropsOrArray] =
    encodeJson.contramap[JSONSchemaPropsOrArray](_.json)
}
