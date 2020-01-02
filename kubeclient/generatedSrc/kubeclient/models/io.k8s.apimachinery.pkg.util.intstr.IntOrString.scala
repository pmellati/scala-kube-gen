package kubeclient.io.k8s.apimachinery.pkg.util.intstr

import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson

/**
  * The OpenAPI specification based on which this file was generated did not contain a definition
  * for this class. Therefore, we simply define the class as a thin wrapper around a json value.
  */
case class IntOrString(
    json: Json
)

object IntOrString {
  implicit val `io.k8s.apimachinery.pkg.util.intstr.IntOrString-Decoder`
      : Decoder[IntOrString] =
    decodeJson.map(json => IntOrString(json))

  implicit val `io.k8s.apimachinery.pkg.util.intstr.IntOrString-Encoder`
      : Encoder[IntOrString] =
    encodeJson.contramap[IntOrString](_.json)
}
