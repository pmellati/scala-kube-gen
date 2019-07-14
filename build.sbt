ThisBuild / scalaVersion     := "2.13.0"
ThisBuild / version          := "0.0.1-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "scala-openapi-gen",
    libraryDependencies ++= Seq(
      "io.swagger"    %  "swagger-parser" % "1.0.44",
      "org.typelevel" %% "cats-effect"    % "2.0.0-M4",
      "org.specs2"    %% "specs2-core"    % "4.6.0" % "test"
    ),
    (scalacOptions in Test) ++= Seq("-Yrangepos")
  )
