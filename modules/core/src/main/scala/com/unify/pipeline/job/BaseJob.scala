package com.unify.pipeline.job

import com.unify.pipeline.schema.Pipeline.Payload
import com.unify.pipeline.schema.UnifyConfig
import com.unify.pipeline.session.SessionInitializer
import org.apache.spark.sql.{DataFrame, SparkSession}

trait BaseJob {
  /**
   * Pending
   * -> try catch
   * -> get delta
   */
  /**
   * Takes program arguments and should return case class UnifyConfig
   *
   * @param arguments Program arguments
   * @return UnifyConfig
   */
  def getConfig(arguments: Array[String]): UnifyConfig

  /**
   * Reads data via config
   *
   * @param config UnifyConfig
   * @return Dataframe
   */
  def extract(spark: SparkSession, config: UnifyConfig): DataFrame

  /**
   * Transforms data as per config
   *
   * @param dataFrame Dataframe
   * @param config    UnifyConfig
   * @return
   */
  def transform(dataFrame: DataFrame, config: Payload): DataFrame

  /**
   * Writes data to target as per config
   *
   * @param dataFrame dataframe
   * @param config    UnifyConfig
   * @return
   */
  def load(dataFrame: DataFrame, config: Payload): Boolean

  /**
   * Main method
   *
   * @param args Program arguments
   */
  def main(args: Array[String]): Unit = {
    val config = getConfig(args)
    val spark = SessionInitializer.create(config.appConfig)
    val dataFrame = extract(spark, config)
    val transformedDataFrame = transform(dataFrame, config.pipeline)
    load(transformedDataFrame, config.pipeline)
  }
}
