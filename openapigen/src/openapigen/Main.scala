package openapigen

import java.nio.file.Path

import cats.effect.IO

import io.swagger.parser.SwaggerParser
import io.swagger.models._

import scopt.{OParser, Read}

import openapigen.write.api.writeApiFiles
import openapigen.write.model.writeModelFiles

object Main {
  case class CliArgs(swaggerFile: Path, apisOutDir: Path, modelsOutDir: Path)

  implicit val pathRead: Read[Path] = Read.fileRead.map(_.toPath)

  val cliArgsParser: OParser[Unit, CliArgs] = {
    val builder = OParser.builder[CliArgs]
    import builder._

    OParser.sequence(
      programName("openapigen"),
      opt[Path]('s', "swagger")
        .action((path, args) => args.copy(swaggerFile = path))
        .text("path to swagger file")
        .required,
      opt[Path]('a', "apis-dir")
        .action((path, args) => args.copy(apisOutDir = path))
        .text("directory to write API files in")
        .required,
      opt[Path]('m', "models-dir")
        .action((path, args) => args.copy(modelsOutDir = path))
        .text("directory to write API files in")
        .required,
    )
  }

  def main(args: Array[String]): Unit = {
    OParser.parse(cliArgsParser, args, CliArgs(null, null, null)) match {
      case Some(args) =>
        run(args).unsafeRunSync()
      case _ =>
        // arguments are bad, error message will have been displayed
    }
  }

  def run(config: CliArgs): IO[Unit] = {
    val readSwagger = IO { new SwaggerParser().read(config.swaggerFile.toString) }

    for {
      swagger <- readSwagger
      _       <- writeApiFiles(swagger, config.apisOutDir)
      _       <- writeModelFiles(swagger, config.modelsOutDir)
    } yield ()
  }
}
