package kubegen;

import scala.collection.JavaConverters._

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

    println("=====================================")
    println(writeModel(nodeModelName, nodeModel))
    println("=====================================")
  }

  def writeModel(fullName: String, m: Model): String = {
    val packageName = fullName.split('.').init.mkString(".")
    val simpleName  = fullName.split('.').last

    val properties = m.getProperties.asScala.toMap

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

  def writeProperty(name: String, p: Property): String = {
    s"$name: ${writePropertyType(p)}"
  }

  def writePropertyType(p: Property): String = {
    val typeWithoutOptionisation = p match {
      case p: StringProperty =>
        "String"
      case p: RefProperty =>
        val fullyQualifiedName = p.getOriginalRef
        // TODO: import the other class.
        simpleNameOf(fullyQualifiedName)
      case p =>
        throw new NotImplementedError(s"Property of class: ${p.getClass}")
    }

    if(p.getRequired)
      typeWithoutOptionisation
    else
      s"Option[$typeWithoutOptionisation] = None"
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