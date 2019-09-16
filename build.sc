import mill._, scalalib._

object openapigen extends ScalaModule {
  def scalaVersion = "2.13.0"

  def scalacOptions = Seq(
    "-deprecation"
  )

  def ivyDeps = Agg(
    ivy"io.swagger:swagger-parser:1.0.44",
    ivy"org.typelevel::cats-effect:2.0.0-M4"
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