package kubeclient.io.k8s.apimachinery.pkg.apis.meta.v1

import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson

/**
  * The OpenAPI specification based on which this file was generated did not contain a definition
  * for this class. Therefore, we simply define the class as a thin wrapper around a json value.
  */
case class Time(
    json: Json
)

object Time {
  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.Time-Decoder`
      : Decoder[Time] =
    decodeJson.map(json => Time(json))

  implicit val `io.k8s.apimachinery.pkg.apis.meta.v1.Time-Encoder`
      : Encoder[Time] =
    encodeJson.contramap[Time](_.json)
}
