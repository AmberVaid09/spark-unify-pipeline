package com.unify.pipeline.test.util

import com.unify.pipeline.schema.Application.FileIo
import com.unify.pipeline.schema.{Application, Pipeline, UnifyConfig}

import java.util.UUID

trait DefaultConfig {

  def getUUID : String = UUID.randomUUID().toString
  val dump : String = s"modules/core/target/temp_output/$getUUID"

  val applicationConfig: Application.Config = Application.Config(
    sparkOptions = Some(Map("key" -> "value", "spark.app.name" -> "unify", "spark.master" -> "local")),
    cloudAlias = Some(Map("gcp" -> Map("gcp.key" -> "gcp_pass"), "aws" -> Map("aws.key" -> "aws_pass"))),
    fileIo = Some(FileIo(input = Map("csv" -> Map("header" -> "true")), output = Map("json" -> Map("header" -> "false")))))

  val pipelinePayload: Pipeline.Payload = Pipeline.Payload(
    name = "emp_tbl",
    source = Pipeline.SourceStorage(
      path = "modules/core/src/test/resources/data/input/file_read_test/random_data.csv",
      fileFormat = "csv"
    ),
    target = Pipeline.TargetStorage(
      path = s"$dump/file_output_test/",
      fileFormat = "orc",
      saveMode = "overwrite",
      partition = None
    ),
    columns = Seq(
      Pipeline.ColumnMetaData(
        sourceName = "name",
        targetName = Some("emp_name"),
        targetType = Some("string"),
        transformation = None),
      Pipeline.ColumnMetaData(
        sourceName = "email",
        targetName = Some("email"),
        targetType = Some("string"),
        transformation = None),
      Pipeline.ColumnMetaData(
        sourceName = "region",
        targetName = Some("state"),
        targetType = Some("string"),
        transformation = None),
      Pipeline.ColumnMetaData(
        sourceName = "text",
        targetName = Some("desc"),
        targetType = Some("string"),
        transformation = Some(Pipeline.ToLower)),
      Pipeline.ColumnMetaData(
        sourceName = "currency",
        targetName = Some("expenditure"),
        targetType = Some("decimal(10,3)"),
        transformation = Some(Pipeline.RegexExp(pattern = "\\$", replacement = "")))
    )
  )

  val unifyConfig: UnifyConfig = UnifyConfig(
    appConfig = applicationConfig,
    pipeline = pipelinePayload
  )

}
