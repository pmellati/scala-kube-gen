ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.0.1-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "kubegen",
    libraryDependencies ++= Seq(
      "io.swagger" % "swagger-parser" % "1.0.44"
    )
  )
