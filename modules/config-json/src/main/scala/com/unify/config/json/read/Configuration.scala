package com.unify.config.json.read

import com.unify.config.json.schema.{Application, Pipeline, UnifyConfig}
import com.unify.pipeline.constants.ApplicationLogger
import com.unify.pipeline.constants.Constants.{ACTIVE_SYSTEM, DEFAULT_CONFIG_PATH, SYSTEM_CONFIG_PATH}
import com.unify.pipeline.constants.ExceptionInfo.IncorrectConfig
import pureconfig.ConfigSource
import pureconfig.generic.auto._

import scala.collection.JavaConverters._

object Configuration extends ApplicationLogger {

  /**
   * Core responsibilities
   * -> Read from program args and system env
   * -> Validates Config
   * -> Creates Application.Config from systemConfigPath
   * -> Creates Pipeline.Payload from ActiveSystem
   * -> [todo] :- should update secrets from secrets.conf(placeholder) and env pick secrets
   */

  def apply(arguments: Array[String]): UnifyConfig = {
    val argMap = argumentsToMap(arguments)
    val systemEnv = System.getenv()
    val appConfigs = argMap ++ systemEnv.asScala
    validateConfigs(appConfigs)

    val systemConfigPath = appConfigs.getOrElse(SYSTEM_CONFIG_PATH, DEFAULT_CONFIG_PATH)
    if (systemConfigPath == DEFAULT_CONFIG_PATH) logger.warn(s"No config_path found, Reading from $DEFAULT_CONFIG_PATH")

    UnifyConfig(
      appConfig = getSystemConfig(systemConfigPath),
      pipeline = getPipelinePayload(appConfigs(ACTIVE_SYSTEM))
    )
  }

  /**
   * Converts arguments to MAP
   * In case of error throws IncorrectConfig exception
   *
   * @param arguments Program arguments
   * @return Map[String, String]
   */
  def argumentsToMap(arguments: Array[String]): Map[String, String] = arguments.flatMap(argument => {
    val data = argument.split("=")
    if (data.length != 2) throw new IncorrectConfig("Arguments should be passed as key value pair")
    else Map(data.head -> data.last)
  }).toMap

  /**
   * Returns active system which is actual pipeline config to run
   *
   * @param config Config
   * @return String
   */
  def validateConfigs(config: Map[String, String]): Boolean = {
    val mandatoryConfig = Seq(ACTIVE_SYSTEM)

    val missingConfig = (configList: Seq[String]) => configList.filter(param => !config.contains(param))
    val missingMandatoryConfig = missingConfig(mandatoryConfig)

    if (missingMandatoryConfig.nonEmpty)
      throw new IncorrectConfig(s"Missing MandatoryConfig ${missingMandatoryConfig.mkString(",")}")
    else true
  }

  /**
   * Returns back system config
   *
   * @param path Path
   * @return
   */
  def getSystemConfig(path: String): Application.Config =
    ConfigSource.resources(path).loadOrThrow[Application.Config]


  /**
   * Returns back pipeline payload
   *
   * @param path Path
   * @return
   */
  def getPipelinePayload(path: String): Pipeline.Payload =
    ConfigSource.resources(path).loadOrThrow[Pipeline.Payload]

}
