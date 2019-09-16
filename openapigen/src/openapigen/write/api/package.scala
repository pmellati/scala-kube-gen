package openapigen.write

import java.nio.file.Path

import scala.jdk.CollectionConverters._

import cats.effect.IO
import cats.implicits._

import io.swagger.models._, parameters._

import openapigen.util._, ScalaCode._, syntax._, ScalaStringContext.ScalaStringContextImplicit

package object api {
  type OperationTag = String

  def writeApiFiles(swagger: Swagger, outDir: Path): IO[Unit] = {
    val opsByTag = operationsByTag(swagger)

    opsByTag.toList.traverse { case (apiTag, ops) =>
      val filePath = outDir.resolve(s"${toApiObjectName(apiTag)}.scala")
      val fileContent = writeApiFile(apiTag, ops).toLiteral

      createFile(filePath, fileContent)
    }.void
  }

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

  def writeParam(p: Parameter, renamed: ParamRenaming): ScalaCode = {
    val noneAsDefaultVal = if(p.getRequired)
      scala""
    else
      scala" = None"

    scala"${renamed(p).id}: ${writeParamType(p)}$noneAsDefaultVal"
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

  // TODO: May be completely misunderstood.
  def writeType: String => ScalaCode = {
    case "string" => "String".lit
    case "boolean" => "Boolean".lit
    case "integer" => "Int".lit
    case unsupported =>
      throw new NotImplementedError(s"Unsupported API call param type: $unsupported")
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
}
