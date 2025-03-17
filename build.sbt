ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "csci493_spring2025_common"
  )

libraryDependencies += "dev.zio" %% "zio" % "2.0.6"
libraryDependencies += "dev.zio" %% "zio-json" % "0.6.2"
