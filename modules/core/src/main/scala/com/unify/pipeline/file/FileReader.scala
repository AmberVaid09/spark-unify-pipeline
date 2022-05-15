package com.unify.pipeline.file

import com.unify.pipeline.schema.UnifyConfig
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.JavaConverters._

object FileReader {

  /**
   * Reads path based on config
   *
   * @param spark  Spark Session
   * @param config Config params
   * @return DataFrame
   */
  def read(spark: SparkSession, config: UnifyConfig): DataFrame = {
    val source = config.pipeline.source

    val fileConfigs = config.appConfig.fileIo match {
      case Some(io) => io.input.getOrElse(source.fileFormat, Map.empty[String, String])
      case None => Map.empty[String, String]
    }

    read(spark, source.fileFormat, fileConfigs, source.path)
  }

  /**
   * Returns dataframe
   *
   * @param spark      Spark Session
   * @param fileFormat File format
   * @param options    File Options
   * @param path       Path
   * @return DataFrame
   */
  def read(spark: SparkSession, fileFormat: String, options: Map[String, String], path: String): DataFrame =
    spark.read.format(fileFormat).options(options).load(path)


  /**
   * Returns dataframe
   *
   * @param spark      Spark Session
   * @param fileFormat File format
   * @param path       Path
   * @return DataFrame
   */
  def read(spark: SparkSession, fileFormat: String, path: String): DataFrame =
    spark.read.format(fileFormat).load(path)

}
