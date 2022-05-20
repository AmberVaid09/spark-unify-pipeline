package com.unify.config.json.read

import com.unify.pipeline.constants.ExceptionInfo.IncorrectConfig
import com.unify.pipeline.schema.Application.FileIo
import com.unify.pipeline.schema.{Application, Pipeline}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpec

class ConfigurationSpec extends AnyWordSpec with BeforeAndAfterAll {

  "Configuration" should {
    "read spark app conf" in {
      val config = Configuration.getSystemConfig("spark_app.conf")
      val expectedOutput = Application.Config(
        sparkOptions = Some(Map("key" -> "value", "spark.app.name" -> "unify")),
        cloudAlias = Some(Map("gcp" -> Map("gcp.key" -> "pass"), "aws" -> Map("aws.key" -> "pass"))),
        fileIo = Some(FileIo(input = Map("csv" -> Map("header" -> "true")), output = Map("json" -> Map("header" -> "false")))))
      assert(config == expectedOutput)
    }

    "read pipeline params and create payload" in {
      val config = Configuration.getPipelinePayload("pipeline_example_1.conf")
      val expectedOutput = Pipeline.Payload(
        name = "covid_tbl",
        source = Pipeline.SourceStorage(
          path = "/user/path/staging/covid_tbl",
          fileFormat = "json"
        ),
        target = Pipeline.TargetStorage(
          path = "/user/path/standard/covid_tbl",
          fileFormat = "orc",
          saveMode = "overwrite",
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
            transformation = Some(Seq(Pipeline.ToUpper))),
          Pipeline.ColumnMetaData(
            sourceName = "email_address",
            targetName = Some("email_id"),
            targetType = Some("string"),
            transformation = Some(Seq(Pipeline.RegexExp(pattern = "some_pattern", replacement = "some_replacement"))))
        )
      )
      assert(config == expectedOutput)
    }

    "convert arguments to map" in {
      val outputData = Configuration.argumentsToMap(Array("config_path=dev.conf", "active_system=some_source_system"))
      assert(outputData("config_path") == "dev.conf" && outputData("active_system") == "some_source_system")
    }

    "throw exception in argumentsToMap " in {
      assertThrows[IncorrectConfig] {
        Configuration.argumentsToMap(Array("config_path"))
      }
    }

    "return true for validations" in {
      val argMap = Configuration.argumentsToMap(Array("config_path=dev.conf", "active_system=some_source_system"))
      assert(Configuration.validateConfigs(argMap))
    }

    "throw exception in validation" in {
      val argMap = Configuration.argumentsToMap(Array("config_path=dev.conf"))
      assertThrows[IncorrectConfig] {
        assert(Configuration.validateConfigs(argMap))
      }
    }

    "return Config from file" in {
      val config = Configuration(Array(s"system_config_path=spark_app.conf", "active_system=pipeline_example_1.conf"))
      assert(config.appConfig.sparkOptions.get("spark.app.name") == "unify" && config.pipeline.name == "covid_tbl")
    }

    "throw exception when read without args" in {
      assertThrows[IncorrectConfig] {
        Configuration(Array())
      }
    }
  }
}
