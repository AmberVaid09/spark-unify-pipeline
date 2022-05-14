package com.unify.config.json.read

import com.unify.config.json.schema.{Application, Pipeline}
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Configuration {

  def getApplicationConfig(path: String): Application.Config =
    ConfigSource.resources(path).loadOrThrow[Application.Config]


  def getPipelinePayload(path: String): Pipeline.Payload =
    ConfigSource.resources(path).loadOrThrow[Pipeline.Payload]

}
