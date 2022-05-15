package com.unify.config.json.read

import com.unify.config.json.schema.{Application, Pipeline}
import com.unify.pipeline.constants.ExceptionInfo.IncorrectConfig
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec

class ConfigurationSpec extends AnyFlatSpec with BeforeAndAfterAll {

  "Configuration" should "read spark app conf" in {
    val config = Configuration.getSystemConfig("spark_app.conf")
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

  "ReadConfig argumentsToMap" should "convert arguments to map" in {
    val outputData = Configuration.argumentsToMap(Array("config_path=dev.conf", "active_system=some_source_system"))
    assert(outputData("config_path") == "dev.conf" && outputData("active_system") == "some_source_system")
  }

  "ReadConfig argumentsToMap" should "throw exception" in {
    assertThrows[IncorrectConfig] {
      Configuration.argumentsToMap(Array("config_path"))
    }
  }

  "ReadConfig validateConfigs" should "return true" in {
    val argMap = Configuration.argumentsToMap(Array("config_path=dev.conf", "active_system=some_source_system"))
    assert(Configuration.validateConfigs(argMap))
  }

  "ReadConfig validateConfigs" should "throw exception" in {
    val argMap = Configuration.argumentsToMap(Array("config_path=dev.conf"))
    assertThrows[IncorrectConfig] {
      assert(Configuration.validateConfigs(argMap))
    }
  }

  "ReadConfig" should "return Config" in {
    val config = Configuration(Array(s"system_config_path=spark_app.conf", "active_system=pipeline_example_1.conf"))
    assert(config.appConfig.sparkOptions.get("spark.app.name") == "unify" && config.pipeline.name == "covid_tbl")
  }

  "ReadConfig" should " throw exception" in {
    assertThrows[IncorrectConfig] {
      Configuration(Array())
    }
  }


}
