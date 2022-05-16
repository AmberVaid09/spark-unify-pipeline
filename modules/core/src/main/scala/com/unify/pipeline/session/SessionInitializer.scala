package com.unify.pipeline.session

import com.unify.pipeline.schema.Application
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

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
  def create(config: Application.Config): SparkSession = {
    val sparkOptions = config.sparkOptions.getOrElse(Map.empty)
    val sparkConf = new SparkConf().setAll(sparkOptions)
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
  def updateSparkHadoopOptions(spark: SparkSession, config: Application.Config, aliasPath: String): Unit =
    config.cloudAlias.getOrElse(Map.empty).getOrElse(aliasPath, Map.empty).foreach { self =>
      spark.sparkContext.hadoopConfiguration.set(self._1, self._2)
    }

}
