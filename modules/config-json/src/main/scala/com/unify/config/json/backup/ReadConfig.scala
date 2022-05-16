package com.unify.config.json.backup

import com.typesafe.config.{Config, ConfigFactory}
import com.unify.pipeline.constants.ApplicationLogger
import com.unify.pipeline.constants.Constants._
import com.unify.pipeline.constants.ExceptionInfo.IncorrectConfig

import scala.collection.JavaConverters._

@deprecated
object ReadConfig extends ApplicationLogger {

  /**
   * Core responsibilities
   * -> Read from program args
   * -> Read from system env
   * -> Validates Config for activeSystem
   * -> Read configPath
   * -> Get active system from config
   * -> Update config with ConfigPath + ENV + program args
   * -> [todo] :- should update secrets from secrets.conf(placeholder) and env pick secrets
   *
   * @param arguments Program arguments
   * @return Config
   */
  def apply(arguments: Array[String]): Config = {
    val argMap = argumentsToMap(arguments)
    val systemEnv = System.getenv()
    val appConfigs = argMap ++ systemEnv.asScala
    validateConfigs(appConfigs)

    val configPath = appConfigs.getOrElse(CONFIG_PATH, DEFAULT_CONFIG_PATH)
    if (configPath == DEFAULT_CONFIG_PATH) logger.warn(s"No config_path found, Reading from $DEFAULT_CONFIG_PATH")

    ConfigFactory.load(configPath) // update secrets with fallback with config (secrets)
      .withFallback(ConfigFactory.parseString(mapToJSON(appConfigs, "program_arguments")))
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
   * Converts Map to JSON
   *
   * @param data      MAP
   * @param parentKey Outer Key
   * @return String
   */
  def mapToJSON(data: Map[String, String], parentKey: String): String = {
    val filteredConfig = Seq(ACTIVE_SYSTEM, CONFIG_PATH)
    val innerData = data.filter(value => filteredConfig.contains(value._1)).map { value =>
      value._1 + " : " + value._2
    }.mkString(",")
    s"{ $parentKey : {$innerData} }"
  }

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

  def updateSecrets(config: Config, appArgs: Map[String, String]): Map[String, String] = {
    config.getConfig(SECRETS).entrySet().asScala.map(_.getKey).flatMap { self =>
      Map(self -> appArgs.getOrElse(self, ""))
    }
  }.toMap

}
