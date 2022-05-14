package com.unify.config.json.read

import com.unify.config.json.schema.{Application, Pipeline}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec

class ConfigurationSpec extends AnyFlatSpec with BeforeAndAfterAll {

  "Configuration" should "read spark app conf" in {
    val config = Configuration.getApplicationConfig("spark_app.conf")
    val expectedOutput = Application.Config(
      sparkOptions = Some(Map("key" -> "value", "spark.app.name" -> "unify")),
      cloudAlias = Some(Map("gcp" -> Map("gcp.key" -> "pass"), "aws" -> Map("aws.key" -> "pass"))))
    assert(config == expectedOutput)
  }

  "Configuration" should "read pipeline params and create payload" in {
    val config = Configuration.getPipelinePayload("pipeline_example_1.conf")
    val expectedOutput = Pipeline.Payload(
      name = "covid_tbl",
      source = Pipeline.Storage(
        path = "/user/path/staging/covid_tbl",
        fileFormat = "json",
        partition = None
      ),
      target = Pipeline.Storage(
        path = "/user/path/standard/covid_tbl",
        fileFormat = "orc",
        partition = Some("part_key")
      ),
      columns = Seq(
        Pipeline.ColumnMetaData(
          sourceName = "id",
          targetName = None,
          targetType = Some("int"),
          transformation = None),
        Pipeline.ColumnMetaData(
          sourceName = "person_name",
          targetName = Some("name"),
          targetType = Some("string"),
          transformation = Some(Pipeline.ToUpper)),
        Pipeline.ColumnMetaData(
          sourceName = "email_address",
          targetName = Some("email_id"),
          targetType = Some("string"),
          transformation = Some(Pipeline.RegexExp(pattern = "some_pattern", replacement = "some_replacement")))
      )
    )
    assert(config == expectedOutput)
  }

}
