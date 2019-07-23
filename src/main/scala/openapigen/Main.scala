package openapigen

import java.nio.file.{Path, Paths}
import java.io.PrintWriter

import scala.collection.JavaConverters._

import cats.effect.IO
import cats.implicits._

import io.swagger.parser.SwaggerParser
import io.swagger.models._, parameters._, properties._

import openapigen.ScalaStringContext.ScalaStringContextImplicit
import openapigen.ScalaCode._, syntax._
import openapigen.Scala._

object Main {
  def main(args: Array[String]) {
    val swagger = new SwaggerParser().read("/Users/pouria/Documents/kube-openapi-spec.json")

    val outputProjectDir = Paths.get("/Users/pouria/src/sample-scala-client/src/main/scala/example")

    // val nodeStatusPath         = swagger.getPath("/api/v1/nodes/{name}/status")
    // val getNodeStatusOperation = nodeStatusPath.getGet
    // val getNodeStOperationFilePath = outputProjectDir.resolve("apis").resolve(s"${getNodeStatusOperation.getOperationId}.scala")

    // createFile(
    //   getNodeStOperationFilePath,
    //   writeOperation("/api/v1/nodes/{name}/status", HttpMethod.GET, getNodeStatusOperation).toLiteral
    // ).unsafeRunSync()

    val opsByTag = operationsByTag(swagger)

    val createApiFiles = opsByTag.toList.traverse { case (apiTag, ops) =>
      val filePath = outputProjectDir.resolve("apis").resolve(s"${toApiObjectName(apiTag)}.scala")
      val fileContent = writeApiFile(apiTag, ops).toLiteral

      createFile(filePath, fileContent)
    }

    createApiFiles.unsafeRunSync()

    val apiTag = "core_v1"
    val nodeApiOps = opsByTag(apiTag)
    val nodeApiFilePath = outputProjectDir.resolve("apis").resolve(s"${toApiObjectName(apiTag)}.scala")
    createFile(
      nodeApiFilePath,
      writeApiFile(apiTag, nodeApiOps).toLiteral
    ).unsafeRunSync()

    val definitions   = swagger.getDefinitions.asScala.toMap

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

  // TODO: find a better name for this.
  case class OperationWithMeta(operation: Operation, path: String, method: HttpMethod)
  type OperationTag = String

  def operationsByTag(swagger: Swagger): Map[OperationTag, List[OperationWithMeta]] = {
    val operationByTag = for {
      pathAndDesc     <- swagger.getPaths.asScala.toSeq
      (path, pathDesc) = pathAndDesc
      methodAndOp     <- pathDesc.getOperationMap.asScala.toSeq
      (httpMethod, op) = methodAndOp
      _                = if(op.getTags.size != 1)
                           throw new NotImplementedError(
                             s"Each operation is expected to have exactly 1 tag. " +
                             s"Operation ${op.getOperationId} has ${op.getTags.size}.")
      tag             <- op.getTags.asScala.toSeq
      opWithMeta       = OperationWithMeta(op, path, httpMethod)
    } yield (tag, opWithMeta)

    operationByTag.groupBy { case (tag, _) =>
      tag
    }.view.mapValues(_.toList.map { case (_, op) =>
      op
    }).toMap
  }

  def toApiObjectName(tag: OperationTag): String = {
    def capitaliseFirstChar(s: String) =
      s.headOption.fold(ifEmpty = "") { head =>
        head.toString.capitalize + s.tail
      }

    def underscoresToPascalCase(s: String) =
      s.split("_").map(capitaliseFirstChar).mkString

    s"${underscoresToPascalCase(tag)}Api"
  }

  def writeApiFile(tag: OperationTag, operations: List[OperationWithMeta]): ScalaCode = {
    val functions = operations.map(writeOperation).mkScala("\n".lit)

    val apiObjectName = toApiObjectName(tag).id

    scala"""
      package myapi

      import cats.Applicative
      import cats.data.Kleisli
      import cats.effect.Sync

      import io.circe._

      import openapigen.ClientConfig

      import org.http4s._, circe._, client.Client

      $importsAnchor

      object $apiObjectName {
        private implicit def circeEncoderToHttp4sEntityEncoder[F[_] : Applicative, A : Encoder]: EntityEncoder[F, A] = jsonEncoderOf
        private implicit def circeDecoderToHttp4sEntityDecoder[F[_] : Sync, A : Decoder]: EntityDecoder[F, A] = jsonOf

        type Action[F[_], A] = Kleisli[F, ClientConfig[F], A]

        $functions
      }
    """
  }

  /** Between different types of swagger params, like path, query and body params, the swagger
   *  spec may use duplicate names. A ParamRenaming maps each param to a unique name.
   */
  type ParamRenaming = Parameter => String

  def writeOperation(opWithMeta: OperationWithMeta): ScalaCode = {
    val op = opWithMeta.operation

    val params = op.getParameters.asScala.toList

    val responses = op.getResponses.asScala.toMap

    // TODO: Should be handled better.
    val okResultType = responses.get("200") match {
      case Some(response) =>
        writeModelType(response.getResponseSchema)
      case None =>
        println(s"WARN: Operation ${op.getOperationId} does not have a 200 response. Defaulting to response type 'Unit'.")
        scala"Unit"
    }

    // Extremely naive renaming that will try to rename query parameters that have the same
    // name as path parameters.
    val paramRenaming: ParamRenaming = {
      val pathParamNames = params.collect {
        case p: PathParameter => p.getName
      }.toSet

      (p: Parameter) => p match {
        case p: QueryParameter if pathParamNames.contains(p.getName) =>
          s"${p.getName}_2"
        case _ =>
          p.getName
      }
    }

    val bodyParam = params.collectFirst {
      case param: BodyParameter => param
    }

    val paramsDecl: ScalaCode = {
      if(params.isEmpty)
        scala""
      else
        scala"(${params.map(p => writeParam(p, paramRenaming)).mkScala(", ".lit)})"
    }

    val pathWithScalaStrInterpolation = "s\"\"\"" + opWithMeta.path.replaceAllLiterally("{", "${") + "\"\"\""

    val uriQueryParamsAdditionCode: ScalaCode = params.collect {
      case p: QueryParameter =>
        if(p.getRequired)
          scala""".withQueryParam("${p.getName.lit}", ${paramRenaming(p).id})"""
        else
          scala""".withOptionQueryParam("${p.getName.lit}", ${paramRenaming(p).id})"""
    }.mkScala("\n".lit)

    val addOptionalBody = bodyParam.fold(ifEmpty = scala"") { bodyParam =>
      val paramName = paramRenaming(bodyParam).id

      scala".withEntity($paramName)"
    }

    val paramsScaladocs = params.map { p =>
      scala"*  @param ${paramRenaming(p).id} ${p.getDescription.lit}"
    }.mkScala("\n".lit)

    val scaladocs = scala"""
      /** ${op.getDescription.lit}
       *
       $paramsScaladocs
       */
    """

    scala"""
        $scaladocs
        def ${op.getOperationId.id}[F[_] : Applicative : Sync]$paramsDecl: Action[F, $okResultType] = Kleisli { _config =>
          val _path = ${pathWithScalaStrInterpolation.lit}
          val _uri  = _config.baseApiUri.withPath(_path)

          val _uriWithQueryParams = _uri
          $uriQueryParamsAdditionCode

          val _method = Method.${opWithMeta.method.toString.lit}

          val _request = Request[F](
            method = _method,
            uri = _uriWithQueryParams
          )$addOptionalBody

          _config.httpClient.expect[$okResultType](_request)
        }
    """
  }

  // TODO: Needs a better name and documentation.
  // Write a type that refers to the model, rather than a definition of the model.
  def writeModelType(model: Model): ScalaCode = model match {
    case model: RefModel =>
      val fqn = modelRefToSimpleRef(model.getReference)

      simpleNameOf(fqn).id.withImport(fqn)
    case model: ModelImpl =>
      model.getType match {
        case "string" => "String".lit
        case unsupported =>
          throw new NotImplementedError(s"Cannot write a type for ModelImpl with type: $unsupported")
      }
    case _ =>
      throw new NotImplementedError(s"Cannot write a type for model: $model")
  }


  /** Turns a ref like "#/definitions/io.k8s.api.core.v1.Node" into "io.k8s.api.core.v1.Node". */
  def modelRefToSimpleRef(ref: String): String = {
    ref.split('/').last
  }

  def writeParamType(p: Parameter, optionisationIsEnabled: Boolean = true): ScalaCode = {
    val typeWithoutOptionisation: ScalaCode = p match {
      case p: PathParameter =>
        scala"${writeType(p.getType)}"
      case p: QueryParameter =>
        scala"${writeType(p.getType)}"
      case p: BodyParameter =>
        writeModelType(p.getSchema)
      case p =>
        throw new NotImplementedError(s"Type of param: $p")
    }

    if(optionisationIsEnabled && !p.getRequired)
      scala"Option[$typeWithoutOptionisation]"
    else
      typeWithoutOptionisation
  }

  def writeParam(p: Parameter, renamed: ParamRenaming): ScalaCode = {
    val noneAsDefaultVal = if(p.getRequired)
      scala""
    else
      scala" = None"

    scala"${renamed(p).id}: ${writeParamType(p)}$noneAsDefaultVal"
  }

  // TODO: May be completely misunderstood.
  def writeType: String => ScalaCode = {
    case "string" => "String".lit
    case "boolean" => "Boolean".lit
    case "integer" => "Int".lit
    case unsupported =>
      throw new NotImplementedError(s"Unsupported API call param type: $unsupported")
  }

  // TODO: Rename to: 'writeModelDefinition'
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
  def writeObjectModel(fullName: String, m: Model): ScalaCode = {
    val packageName = packageOf(fullName)
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
      writeProperty(name, prop)
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

  def simpleNameOf(fullyQualifiedName: String): String =
    fullyQualifiedName.split('.').last

  def packageOf(fullyQualifiedName: String): String =
    fullyQualifiedName.split('.').init.mkString(".")
}
