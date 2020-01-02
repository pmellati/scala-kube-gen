package kubeclient.io.k8s.`apiextensions-apiserver`.pkg.apis.apiextensions.v1

import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._

case class JSONSchemaProps(
    pattern: Option[String] = None,
    additionalItems: Option[JSONSchemaPropsOrBool] = None,
    maxProperties: Option[Long] = None,
    id: Option[String] = None,
    properties: Option[Map[String, JSONSchemaProps]] = None,
    exclusiveMinimum: Option[Boolean] = None,
    `x-kubernetes-list-map-keys`: Option[List[String]] = None,
    allOf: Option[List[JSONSchemaProps]] = None,
    exclusiveMaximum: Option[Boolean] = None,
    format: Option[String] = None,
    $ref: Option[String] = None,
    nullable: Option[Boolean] = None,
    description: Option[String] = None,
    anyOf: Option[List[JSONSchemaProps]] = None,
    `x-kubernetes-list-type`: Option[String] = None,
    patternProperties: Option[Map[String, JSONSchemaProps]] = None,
    items: Option[JSONSchemaPropsOrArray] = None,
    maxItems: Option[Long] = None,
    `x-kubernetes-int-or-string`: Option[Boolean] = None,
    `x-kubernetes-embedded-resource`: Option[Boolean] = None,
    maximum: Option[Double] = None,
    multipleOf: Option[Double] = None,
    enum: Option[List[JSON]] = None,
    `x-kubernetes-preserve-unknown-fields`: Option[Boolean] = None,
    additionalProperties: Option[JSONSchemaPropsOrBool] = None,
    default: Option[JSON] = None,
    minItems: Option[Long] = None,
    not: Option[JSONSchemaProps] = None,
    definitions: Option[Map[String, JSONSchemaProps]] = None,
    minLength: Option[Long] = None,
    title: Option[String] = None,
    minimum: Option[Double] = None,
    `type`: Option[String] = None,
    required: Option[List[String]] = None,
    example: Option[JSON] = None,
    $schema: Option[String] = None,
    oneOf: Option[List[JSONSchemaProps]] = None,
    uniqueItems: Option[Boolean] = None,
    minProperties: Option[Long] = None,
    dependencies: Option[Map[String, JSONSchemaPropsOrStringArray]] = None,
    externalDocs: Option[ExternalDocumentation] = None,
    maxLength: Option[Long] = None
)

object JSONSchemaProps {
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.JSONSchemaProps-Decoder`
      : Decoder[JSONSchemaProps] = deriveDecoder
  implicit val `io.k8s.apiextensions-apiserver.pkg.apis.apiextensions.v1.JSONSchemaProps-Encoder`
      : Encoder[JSONSchemaProps] = deriveEncoder
}
