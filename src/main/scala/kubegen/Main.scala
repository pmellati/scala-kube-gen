package kubegen;

import java.nio.file.{Path, Paths}
import java.io.PrintWriter

import scala.collection.JavaConverters._

import cats.effect.IO
import cats.implicits._

import io.swagger.parser.SwaggerParser
import io.swagger.models._, parameters._, properties._

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

    val outputProjectDir = Paths.get("/var/tmp/outt")

    val writeModelFiles = definitions.toList.traverse { case (modelName, model) =>
      val filePath: Path = outputProjectDir.resolve("models").resolve(s"$modelName.scala")
      val fileContent: String = writeModel(modelName, model)
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
  def writeModel(fullName: String, model: Model): String = {
    // The 'Model' interface lacks essential methods. So, cast to the only implementation.
    val m = model.asInstanceOf[ModelImpl]

    m.getType match {
      case "object" => 
        writeObjectModel(fullName, m)
      case "string" =>
        println(s"WARN: model $fullName has type 'string'. Not implemented yet.")
        "// string model type - what do we need here?"
      case null =>
        println(s"WARN: model $fullName has type 'null'. Not implemented yet.")
        "// NULL model type - what do we need here?"
        // throw new NotImplementedError(s"Model type: NULL\nModel name: $fullName")
      case other =>
        throw new NotImplementedError(s"Model type: $other\nModel name: $fullName")
    }
  }

  /** Write a `Model` where `.getType` returns "object". */
  def writeObjectModel(fullName: String, m: Model): String = {
    val packageName = fullName.split('.').init.mkString(".")
    val simpleName  = fullName.split('.').last

    val properties: Map[String, Property] = Option(m.getProperties) match {
      case None => 
        // TODO: Use a logging lib.
        println(s"WARN: properties of 'object' model $fullName is 'null'. Empty case class will be generated.")
        Map.empty
      case Some(javaMap) =>
        javaMap.asScala.toMap
    }


    val fieldsScalaCode = properties.map { case (name, prop) =>
      s"${writeProperty(name, prop)}"
    }.mkString(",\n  ")

    val modelImports = properties.values.toList.collect {
      case p: RefProperty if packageOf(p.getSimpleRef) != packageName =>
        s"import ${p.getSimpleRef}"
    }.mkString("\n")

    s"""
      |package $packageName
      |
      |$modelImports
      |
      |import cats.effect.IO
      |
      |import org.http4s.{EntityDecoder, EntityEncoder}
      |import org.http4s.circe._
      |
      |import io.circe.{Encoder, Decoder}
      |import io.circe.generic.semiauto._
      |
      |case class $simpleName(
      |  $fieldsScalaCode
      |)
      |
      |object $simpleName {
      |  implicit val `${fullName}-Decoder`: Decoder[$simpleName] = deriveDecoder
      |  implicit val `${fullName}-Encoder`: Encoder[$simpleName] = deriveEncoder
      |
      |  implicit val `${fullName}-EntityDecoder`: EntityDecoder[IO, $simpleName] = jsonOf
      |  implicit val `${fullName}-EntityEncoder`: EntityEncoder[IO, $simpleName] = jsonEncoderOf
      |}
    """.stripMargin
  }

  def writeProperty(name: String, p: Property): String =
    s"$name: ${writePropertyType(p)}"

  // TODO: Not exhaustive.
  // TODO: Incorrectly named: not returning a type when suffixing with '= None'.
  def writePropertyType(p: Property, optionisationIsEnabled: Boolean = true): String = {
    val typeWithoutOptionisation = p match {
      case _: StringProperty =>
        "String"
      case _: IntegerProperty =>
        "Int"
      case _: LongProperty =>
        "Long"
      case _: DoubleProperty =>
        "Double"
      case _: BooleanProperty =>
        "Boolean"
      case p: RefProperty =>
        val fullyQualifiedName = p.getOriginalRef
        // TODO: import the other class.
        simpleNameOf(fullyQualifiedName)
      case p: ArrayProperty => 
        val elementType = writePropertyType(p.getItems, optionisationIsEnabled = false)
        s"List[$elementType]"
      case p: MapProperty =>
        val valueType = writePropertyType(p.getAdditionalProperties, optionisationIsEnabled = false)
        s"Map[String, $valueType]"  // Note: Key type is always string in an open-api map.
      case p =>
        throw new NotImplementedError(s"Property of class: ${p.getClass}")
    }

    if(optionisationIsEnabled && !p.getRequired)
      s"Option[$typeWithoutOptionisation] = None"
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