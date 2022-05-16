import Version._
import sbt._

object Dependencies {

  lazy val sparkDependencies = Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
  )

  lazy val configDependencies = Seq(
    "com.typesafe" % "config" % configVersion,
    "com.github.pureconfig" % "pureconfig_2.12" % pureConfigVersion
  )

  lazy val logDependencies = "ch.qos.logback" % "logback-classic" % logsVersion
  lazy val testDependencies = "org.scalatest" %% "scalatest" % testVersion % Test

}
