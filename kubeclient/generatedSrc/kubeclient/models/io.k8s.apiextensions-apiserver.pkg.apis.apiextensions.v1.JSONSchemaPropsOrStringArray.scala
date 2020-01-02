package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson

/**
  * The OpenAPI specification based on which this file was generated did not contain a definition
  * for this class. Therefore, we simply define the class as a thin wrapper around a json value.
  */
case class JSONSchemaPropsOrStringArray(
    json: Json
)

object JSONSchemaPropsOrStringArray {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.JSONSchemaPropsOrStringArray-Decoder`
      : Decoder[JSONSchemaPropsOrStringArray] =
    decodeJson.map(json => JSONSchemaPropsOrStringArray(json))

  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.JSONSchemaPropsOrStringArray-Encoder`
      : Encoder[JSONSchemaPropsOrStringArray] =
    encodeJson.contramap[JSONSchemaPropsOrStringArray](_.json)
}
