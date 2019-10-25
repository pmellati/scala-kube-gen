package openapigen.write

import java.nio.file.Path

import scala.jdk.CollectionConverters._

import cats.effect.IO
import cats.implicits._

import io.swagger.models._, properties._

import openapigen.util._, ScalaCode._, syntax._, ScalaStringContext.ScalaStringContextImplicit

package object model {
  def writeModelFiles(swagger: Swagger, outDir: Path, basePackage: String): IO[Unit] = {
    val definitions = swagger.getDefinitions.asScala.toMap

    definitions.toList.traverse { case (modelName, model) =>
      val filePath: Path      = outDir.resolve(s"$modelName.scala")
      val fileContent: String = writeModel(modelName, model, basePackage).toLiteral
      createFile(filePath, fileContent)
    }.void
  }

  // TODO: Rename to: 'writeModelDefinition'
  // TODO: Move all model translation code into separate object.
  def writeModel(fullName: String, model: Model, basePackage: String): ScalaCode = {
    // The 'Model' interface lacks essential methods. So, cast to the only implementation.
    val m = model.asInstanceOf[ModelImpl]

    m.getType match {
      case "object" =>
        writeObjectModel(fullName, m, basePackage)
      case "string" =>
        println(s"WARN: model $fullName has type 'string'. Generating thin json wrapper.")
        // TODO: For more user convenience, write a value class wrapping a String
        writeThinJsonWrapperModel(fullName, basePackage)
      case null =>
        println(s"WARN: model $fullName has type 'null'. Generating thin json wrapper.")
        writeThinJsonWrapperModel(fullName, basePackage)
      case other =>
        throw new NotImplementedError(s"Model type: $other\nModel name: $fullName")
    }
  }

  /** Write a `Model` as a thin wrapper around a json value, because either the OpenAPI spec
   *  has provided an empty definition for this model, or we don't know how translate this
   *  type of model yet. */
  def writeThinJsonWrapperModel(fullName: String, basePackage: String): ScalaCode = {
    val packageName = s"$basePackage.${packageOf(fullName)}".fqn
    val simpleName  = simpleNameOf(fullName).id

    scala"""
      package $packageName

      import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson

      /**
       * The OpenAPI specification based on which this file was generated did not contain a definition
       * for this class. Therefore, we simply define the class as a thin wrapper around a json value.
       */
      case class $simpleName(
        json: Json
      )

      object $simpleName {
        implicit val `${fullName.lit}-Decoder`: Decoder[$simpleName] =
          decodeJson.map(json => $simpleName(json))

        implicit val `${fullName.lit}-Encoder`: Encoder[$simpleName] =
          encodeJson.contramap[$simpleName](_.json)
      }
    """
  }

  /** Write a `Model` where `.getType` returns "object". */
  def writeObjectModel(fullName: String, m: Model, basePackage: String): ScalaCode = {
    val packageName = s"$basePackage.${packageOf(fullName)}"
    val simpleName  = simpleNameOf(fullName).id

    val properties: Map[String, Property] = Option(m.getProperties) match {
      case None =>
        // TODO: Use a logging lib.
        // TODO: Write a thin json wrapper instead.
        println(s"WARN: properties of 'object' model $fullName is 'null'. Empty case class will be generated.")
        Map.empty
      case Some(javaMap) =>
        javaMap.asScala.toMap
    }

    val fields: ScalaCode = properties.map { case (name, prop) =>
      writeProperty(name, prop, basePackage)
    }.mkScala(",\n  ".lit)

    scala"""
      package ${packageName.fqn}

      $importsAnchor

      import io.circe.{Encoder, Decoder}
      import io.circe.generic.semiauto._

      case class $simpleName(
        $fields
      )

      object $simpleName {
        implicit val `${fullName.lit}-Decoder`: Decoder[$simpleName] = deriveDecoder
        implicit val `${fullName.lit}-Encoder`: Encoder[$simpleName] = deriveEncoder
      }
    """.filterImports(
      packageOf(_) != packageName
    )
  }

  def writeProperty(name: String, p: Property, basePackage: String): ScalaCode =
    scala"${name.id}: ${writePropertyType(p, basePackage)}"

  // TODO: Not exhaustive.
  // TODO: Incorrectly named: not returning a type when suffixing with '= None'.
  def writePropertyType(p: Property, basePackage: String, optionisationIsEnabled: Boolean = true): ScalaCode = {
    val typeWithoutOptionisation: ScalaCode = p match {
      case _: StringProperty =>
        "String".id
      case _: IntegerProperty =>
        "Int".id
      case _: LongProperty =>
        "Long".id
      case _: DoubleProperty =>
        "Double".id
      case _: BooleanProperty =>
        "Boolean".id
      case p: RefProperty =>
        val fullyQualifiedName = s"$basePackage.${p.getSimpleRef}"
        simpleNameOf(fullyQualifiedName).id.withImport(fullyQualifiedName)
      case p: ArrayProperty =>
        val elementType = writePropertyType(p.getItems, basePackage, optionisationIsEnabled = false)
        scala"List[$elementType]"
      case p: MapProperty =>
        val valueType = writePropertyType(p.getAdditionalProperties, basePackage, optionisationIsEnabled = false)
        scala"Map[String, $valueType]"  // Note: Key type is always string in an open-api map.
      case p =>
        throw new NotImplementedError(s"Property of class: ${p.getClass}")
    }

    if(optionisationIsEnabled && !p.getRequired)
      scala"Option[$typeWithoutOptionisation] = None"
    else
      typeWithoutOptionisation
  }
}
