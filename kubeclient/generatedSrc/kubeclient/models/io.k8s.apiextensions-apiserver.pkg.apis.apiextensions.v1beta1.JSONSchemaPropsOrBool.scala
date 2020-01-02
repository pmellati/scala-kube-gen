package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1beta1

import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson

/**
  * The OpenAPI specification based on which this file was generated did not contain a definition
  * for this class. Therefore, we simply define the class as a thin wrapper around a json value.
  */
case class JSONSchemaPropsOrBool(
    json: Json
)

object JSONSchemaPropsOrBool {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.JSONSchemaPropsOrBool-Decoder`
      : Decoder[JSONSchemaPropsOrBool] =
    decodeJson.map(json => JSONSchemaPropsOrBool(json))

  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1beta1.JSONSchemaPropsOrBool-Encoder`
      : Encoder[JSONSchemaPropsOrBool] =
    encodeJson.contramap[JSONSchemaPropsOrBool](_.json)
}
