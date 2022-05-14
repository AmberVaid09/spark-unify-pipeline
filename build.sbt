import Dependencies._
import Version._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.15"
ThisBuild / organization := "com.unify"
ThisBuild / organizationName := "SparkUnifyPipeline"

lazy val subProjectName = "modules"
lazy val subProjectNameExamples = "examples"
lazy val scalaVersionsToCompile = scalaVersions

lazy val root = (project in file("."))
  .settings(name := "spark-unify-pipeline")
  .aggregate(core, configJSON)

lazy val core = (project in file(s"$subProjectName/core"))
  .settings(
    name := "core",
    crossScalaVersions := scalaVersionsToCompile,
    libraryDependencies ++= sparkDependencies ++ configDependencies :+ logDependencies :+ testDependencies
  )

lazy val configJSON = (project in file(s"$subProjectName/config-json"))
  .settings(
    name := "config-json",
    crossScalaVersions := scalaVersionsToCompile,
    libraryDependencies ++= configDependencies :+ testDependencies
  ).dependsOn(core)
