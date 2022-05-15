package com.unify.pipeline.test.util

import com.unify.pipeline.schema.Application

trait DefaultConfig {

  val applicationConfig: Application.Config = Application.Config(
    sparkOptions = Some(Map("spark.master" -> "local", "spark.app.name" -> "unify")),
    cloudAlias = Some(Map("gcp" -> Map("gcp.key" -> "gcp_pass"), "aws" -> Map("aws.key" -> "aws_pass"))),
    fileIo = None)

}
