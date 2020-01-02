package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson

/**
  * The OpenAPI specification based on which this file was generated did not contain a definition
  * for this class. Therefore, we simply define the class as a thin wrapper around a json value.
  */
case class MicroTime(
    json: Json
)

object MicroTime {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.MicroTime-Decoder`
      : Decoder[MicroTime] =
    decodeJson.map(json => MicroTime(json))

  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.MicroTime-Encoder`
      : Encoder[MicroTime] =
    encodeJson.contramap[MicroTime](_.json)
}
