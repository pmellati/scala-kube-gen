import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._, scalalib._

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
      "-Yrangepos"
    )

    def ivyDeps = Agg(
      ivy"org.specs2::specs2-core:4.6.0"
    )
  }
}

object kubeclient extends ScalaModule {
  override def scalaVersion = "2.12.8"

  override def scalacOptions = Seq(
    "-deprecation",
  )

  val http4sVersion = "0.20.0-RC1"

  override def ivyDeps = Agg(
    ivy"io.circe::circe-generic:0.11.1",
    ivy"org.http4s::http4s-blaze-client:$http4sVersion",
    ivy"org.http4s::http4s-circe:$http4sVersion",
    ivy"org.http4s::http4s-dsl:$http4sVersion",
    ivy"org.typelevel::cats-effect:1.2.0",
  )

  override def generatedSources = T.sources{ millSourcePath / 'generatedSrc }

  object test extends Tests {
    def testFrameworks = Seq(
      "org.specs2.runner.Specs2Framework"
    )

    def scalacOptions = openapigen.scalacOptions() ++ Seq(
      "-Yrangepos"
    )

    def ivyDeps = kubeclient.ivyDeps() ++ Agg(
      ivy"com.lihaoyi::os-lib:0.3.0",
      ivy"org.specs2::specs2-core:4.6.0",
    )
  }
}