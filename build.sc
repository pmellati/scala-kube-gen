import mill._, scalalib._, scalafmt._, define._, publish._

object openapigen extends ScalaModule {
  def scalaVersion = "2.13.0"

  def scalacOptions = Seq(
    "-encoding", "utf8",
    "-deprecation",
    "-unchecked",
    "-Xlint",
  )

  def ivyDeps = Agg(
    ivy"com.github.scopt::scopt:4.0.0-RC2",
    ivy"io.swagger:swagger-parser:1.0.44",
    ivy"org.typelevel::cats-effect:2.0.0-M4",
  )

  object test extends Tests {
    def testFrameworks = Seq(
      "org.specs2.runner.Specs2Framework"
    )

    def scalacOptions = openapigen.scalacOptions() ++ Seq(
      "-Yrangepos"  // required by specs2
    )

    def ivyDeps = Agg(
      ivy"org.specs2::specs2-core:4.6.0"
    )
  }
}

object kubeclient extends ScalaModule with ScalafmtModule with PublishModule {
  override def scalaVersion = "2.12.10"

  override def scalacOptions = Seq(
    "-deprecation",
    "-encoding", "utf8",
    "-language:_",
    "-unchecked",
    "-Xlint",
  )

  val http4sVersion = "0.20.0-RC1"

  override def ivyDeps = Agg(
    ivy"io.circe::circe-generic:0.11.1",
    ivy"io.circe::circe-yaml:0.10.0",
    ivy"org.http4s::http4s-blaze-client:$http4sVersion",
    ivy"org.http4s::http4s-circe:$http4sVersion",
    ivy"org.http4s::http4s-dsl:$http4sVersion",
    ivy"org.typelevel::cats-effect:1.2.0",
  )

  override def generatedSources = T.sources{ millSourcePath / 'generatedSrc }

  // Overriden to format generatedSources, rather than sources.
  override def reformat(): Command[Unit] = T.command {
    ScalafmtWorkerModule
      .worker()
      .reformat(
        filesToFormat(generatedSources()),
        scalafmtConfig().head
      )
  }

  override def pomSettings = PomSettings(
    description = artifactName(),
    organization = "me.pouria",
    url = "https://github.com/pmellati/scala-openapi-gen",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("pmellati", "scala-openapi-gen"),
    developers = Seq(
      Developer("pmellati", "Pouria Mellati", "https://github.com/pmellati")
    )
  )

  override def publishVersion = "0.0.1-SNAPSHOT"

  object test extends Tests {
    def testFrameworks = Seq(
      "org.specs2.runner.Specs2Framework"
    )

    def scalacOptions = openapigen.scalacOptions() ++ Seq(
      "-Yrangepos"  // required by specs2
    )

    def ivyDeps = kubeclient.ivyDeps() ++ Agg(
      ivy"com.lihaoyi::os-lib:0.3.0",
      ivy"org.specs2::specs2-core:4.6.0",
    )
  }
}