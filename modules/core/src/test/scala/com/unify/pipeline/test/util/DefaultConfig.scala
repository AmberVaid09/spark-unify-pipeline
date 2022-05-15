package com.unify.pipeline.test.util

import com.unify.pipeline.schema.Application.FileIo
import com.unify.pipeline.schema.{Application, Pipeline, UnifyConfig}

trait DefaultConfig {

  val applicationConfig: Application.Config = Application.Config(
    sparkOptions = Some(Map("key" -> "value", "spark.app.name" -> "unify")),
    cloudAlias = Some(Map("gcp" -> Map("gcp.key" -> "pass"), "aws" -> Map("aws.key" -> "pass"))),
    fileIo = Some(FileIo(input = Map("csv" -> Map("header" -> "true")), output = Map("json" -> Map("header" -> "false")))))

  val pipelinePayload: Pipeline.Payload = Pipeline.Payload(
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

  val unifyConfig: UnifyConfig = UnifyConfig(
    appConfig = applicationConfig,
    pipeline = pipelinePayload
  )

}
