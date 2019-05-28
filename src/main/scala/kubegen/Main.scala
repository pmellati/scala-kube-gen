package kubegen;

import java.nio.file.{Path, Paths}
import java.io.PrintWriter

import scala.collection.JavaConverters._

import cats.effect.IO
import cats.implicits._

import io.swagger.parser.SwaggerParser
import io.swagger.models._, parameters._, properties._

import kubegen.ScalaStringContext.ScalaStringContextImplicit
import kubegen.ScalaCode._, syntax._
import kubegen.Scala._

object Main {
  def main(args: Array[String]) {
    val swagger = new SwaggerParser().read("/Users/pouria/Documents/kube-openapi-spec.json")
    val nodeStatusPath = swagger.getPath("/api/v1/nodes/{name}/status")
    val getNodeStatus = nodeStatusPath.getGet

    // println("=====================================")
    // println(writeOperation(swagger, getNodeStatus))
    // println("=====================================")

    val definitions   = swagger.getDefinitions.asScala.toMap
    val nodeModelName = "io.k8s.api.core.v1.Node"
    val nodeModel     = definitions(nodeModelName)

    // println("=====================================")
    // println(writeModel(nodeModelName, nodeModel))
    // println("=====================================")

    val outputProjectDir = Paths.get("/Users/pouria/src/sample-scala-client/src/main/scala/example")

    val writeModelFiles = definitions.toList.traverse { case (modelName, model) =>
      val filePath: Path      = outputProjectDir.resolve("models").resolve(s"$modelName.scala")
      val fileContent: String = writeModel(modelName, model).toLiteral
      createFile(filePath, fileContent)
    }

    writeModelFiles.unsafeRunSync()
  }

  // TODO: Use a better library for file system operations.
  def createFile(path: Path, text: String): IO[Unit] = IO {
    path.getParent.toFile.mkdirs()

    val file = path.toFile
    file.createNewFile()

    val writer = new PrintWriter(file)
    writer.write(text)
    writer.close()
  }

  // TODO: Move all model translation code into separate object.
  def writeModel(fullName: String, model: Model): ScalaCode = {
    // The 'Model' interface lacks essential methods. So, cast to the only implementation.
    val m = model.asInstanceOf[ModelImpl]

    m.getType match {
      case "object" => 
        writeObjectModel(fullName, m)
      case "string" =>
        println(s"WARN: model $fullName has type 'string'. Generating thin json wrapper.")
        // TODO: For more user convenience, write a value class wrapping a String
        writeThinJsonWrapperModel(fullName) 
      case null =>
        println(s"WARN: model $fullName has type 'null'. Generating thin json wrapper.")
        writeThinJsonWrapperModel(fullName)
      case other =>
        throw new NotImplementedError(s"Model type: $other\nModel name: $fullName")
    }
  }

  /** Write a `Model` as a thin wrapper around a json value, because either the OpenAPI spec
   *  has provided an empty definition for this model, or we don't know how translate this
   *  type of model yet. */
  def writeThinJsonWrapperModel(fullName: String): ScalaCode = {
    val packageName = packageOf(fullName).fqn
    val simpleName  = simpleNameOf(fullName).id

    scala"""
      package $packageName

      import cats.effect.IO
      
      import org.http4s.{EntityDecoder, EntityEncoder}
      import org.http4s.circe._
      
      import io.circe.{Decoder, Encoder, Json}, Decoder.decodeJson, Encoder.encodeJson
      import io.circe.generic.semiauto._
      
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
      
        implicit val `${fullName.lit}-EntityDecoder`: EntityDecoder[IO, $simpleName] = jsonOf
        implicit val `${fullName.lit}-EntityEncoder`: EntityEncoder[IO, $simpleName] = jsonEncoderOf
      }
    """
  }

  /** Write a `Model` where `.getType` returns "object". */
  def writeObjectModel(fullName: String, m: Model): ScalaCode = {
    val packageName = packageOf(fullName)
    val simpleName  = simpleNameOf(fullName).id

    val properties: Map[String, Property] = Option(m.getProperties) match {
      case None => 
        // TODO: Use a logging lib.
        println(s"WARN: properties of 'object' model $fullName is 'null'. Empty case class will be generated.")
        Map.empty
      case Some(javaMap) =>
        javaMap.asScala.toMap
    }

    val fields: ScalaCode = properties.map { case (name, prop) =>
      writeProperty(name, prop)
    }.mkScala(",\n  ".lit)

    scala"""
      package ${packageName.fqn}
      
      $importsAnchor
      
      import cats.effect.IO
      
      import org.http4s.{EntityDecoder, EntityEncoder}
      import org.http4s.circe._
      
      import io.circe.{Encoder, Decoder}
      import io.circe.generic.semiauto._
      
      case class $simpleName(
        $fields
      )
      
      object $simpleName {
        implicit val `${fullName.lit}-Decoder`: Decoder[$simpleName] = deriveDecoder
        implicit val `${fullName.lit}-Encoder`: Encoder[$simpleName] = deriveEncoder
      
        implicit val `${fullName.lit}-EntityDecoder`: EntityDecoder[IO, $simpleName] = jsonOf
        implicit val `${fullName.lit}-EntityEncoder`: EntityEncoder[IO, $simpleName] = jsonEncoderOf
      }
    """.filterImports(
      packageOf(_) != packageName
    )
  }

  def writeProperty(name: String, p: Property): ScalaCode =
    scala"${name.id}: ${writePropertyType(p)}"

  // TODO: Not exhaustive.
  // TODO: Incorrectly named: not returning a type when suffixing with '= None'.
  def writePropertyType(p: Property, optionisationIsEnabled: Boolean = true): ScalaCode = {
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
        val fullyQualifiedName = p.getSimpleRef
        // TODO: import the other class.
        simpleNameOf(fullyQualifiedName).id.withImport(fullyQualifiedName)
      case p: ArrayProperty => 
        val elementType = writePropertyType(p.getItems, optionisationIsEnabled = false)
        scala"List[$elementType]"
      case p: MapProperty =>
        val valueType = writePropertyType(p.getAdditionalProperties, optionisationIsEnabled = false)
        scala"Map[String, $valueType]"  // Note: Key type is always string in an open-api map.
      case p =>
        throw new NotImplementedError(s"Property of class: ${p.getClass}")
    }

    if(optionisationIsEnabled && !p.getRequired)
      scala"Option[$typeWithoutOptionisation] = None"
    else
      typeWithoutOptionisation
  }

  def writeOperation(swagger: Swagger, op: Operation): String = {
    // TODO: Handle all responses.
    // TODO: Remove parentheses if there are no args.
    // TODO: What if no 200 response?

    val params = op.getParameters.asScala.toList

    val responses = op.getResponses.asScala.toMap

    val okResponse    = responses("200")
    val okResultRef   = okResponse.getResponseSchema.getReference    
    val okResultDefId = okResultRef.split('/').last    // Assuming the ref is like: #/definitions/io.k8s.api.core.v1.Node

    val paramsDeclScala =
      if(params.isEmpty)
        ""
      else
        params.map(writeParam).mkString("(", ", ", ")")

    s"""
      def ${op.getOperationId}$paramsDeclScala: IO[$okResultDefId] = {

        wooow!
      }
    """
  }

  // TODO: Handle default values.
  def writeParam(p: Parameter): String = {
    def paramType(swaggerType: String) =
      if(p.getRequired) writeType(swaggerType)
      else              s"Option[${writeType(swaggerType)}] = None"

    p match {
      case p: PathParameter =>
        s"${p.getName}: ${paramType(p.getType)}"
      case p: QueryParameter =>
        s"${p.getName}: ${paramType(p.getType)}"
      case p =>
        throw new NotImplementedError(s"Param: $p")
    }
  }

  // TODO: May be completely misunderstood.
  def writeType: String => String = {
    case "string" => "String"
  }

  def simpleNameOf(fullyQualifiedName: String): String =
    fullyQualifiedName.split('.').last
  
  def packageOf(fullyQualifiedName: String): String =
    fullyQualifiedName.split('.').init.mkString(".")
}
