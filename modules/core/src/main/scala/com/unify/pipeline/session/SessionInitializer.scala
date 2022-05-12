package com.unify.pipeline.session

import com.typesafe.config.Config
import com.unify.pipeline.constants.Constants
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._

object SessionInitializer {
  /**
   * Core responsibility
   * -> Create spark session
   * -> Read config spark_options and create spark conf
   *
   * @param config Config
   * @return SparkSession
   */
  def create(config: Config): SparkSession = {
    val sparkConf = new SparkConf().setAll(getSparkOptions(config))
    SparkSession.builder().config(sparkConf).getOrCreate()
  }

  /**
   * Returns Spark options
   *
   * @param config Config
   * @return
   */
  def getSparkOptions(config: Config): Map[String, String] = {
    val cleanText = (value : String) => value.replaceAll("\"","")
    config.getConfig(Constants.SPARK_OPTIONS).entrySet().asScala.flatMap { option =>
      Map(cleanText(option.getKey) -> cleanText(option.getValue.render()))
    }.toMap
  }

}
