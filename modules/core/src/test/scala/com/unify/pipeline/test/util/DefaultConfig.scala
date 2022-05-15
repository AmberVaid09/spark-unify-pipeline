package com.unify.pipeline.test.util

import com.unify.pipeline.schema.Application.FileIo
import com.unify.pipeline.schema.{Application, Pipeline, UnifyConfig}

trait DefaultConfig {

  val applicationConfig: Application.Config = Application.Config(
    sparkOptions = Some(Map("key" -> "value", "spark.app.name" -> "unify", "spark.master" -> "local")),
    cloudAlias = Some(Map("gcp" -> Map("gcp.key" -> "gcp_pass"), "aws" -> Map("aws.key" -> "aws_pass"))),
    fileIo = Some(FileIo(input = Map("csv" -> Map("header" -> "true")), output = Map("json" -> Map("header" -> "false")))))

  val pipelinePayload: Pipeline.Payload = Pipeline.Payload(
    name = "emp_tbl",
    source = Pipeline.Storage(
      path = "modules/core/src/test/resources/data/input/file_read_test/random_data.csv",
      fileFormat = "csv",
      partition = None
    ),
    target = Pipeline.Storage(
      path = "modules/core/src/test/resources/output/file_output_test/",
      fileFormat = "orc",
      partition = None
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

  val unifyConfig: UnifyConfig = UnifyConfig(
    appConfig = applicationConfig,
    pipeline = pipelinePayload
  )

}
