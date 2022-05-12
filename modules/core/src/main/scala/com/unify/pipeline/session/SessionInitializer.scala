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
   * -> Updates Hadoop config to support read/write to multiple cloud system.
   *
   * @param config Config
   * @return SparkSession
   */
  def create(config: Config): SparkSession = {
    val sparkConf = new SparkConf().setAll(getSparkOptions(config, Constants.SPARK_OPTIONS))
    SparkSession.builder().config(sparkConf).getOrCreate()
  }

  /**
   * Used to update Spark hadoop option
   * -> Can be used to read data from one cloud source while writing data to different cloud source
   *
   * @param spark     Spark Session
   * @param config    Config
   * @param aliasPath Alias path found in active system for source and target config
   */
  def updateSparkHadoopOptions(spark: SparkSession, config: Config, aliasPath: String): Unit =
    getSparkOptions(config, aliasPath).foreach { self =>
      spark.sparkContext.hadoopConfiguration.set(self._1, self._2)
    }

  /**
   * Returns Spark options
   *
   * @param config Config
   * @return
   */
  def getSparkOptions(config: Config, path: String): Map[String, String] = {
    val cleanText = (value: String) => value.replaceAll("\"", "")
    config.getConfig(path).entrySet().asScala.flatMap { option =>
      Map(cleanText(option.getKey) -> cleanText(option.getValue.render()))
    }.toMap
  }

}
