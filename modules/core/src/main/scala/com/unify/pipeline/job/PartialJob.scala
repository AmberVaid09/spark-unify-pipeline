package com.unify.pipeline.job

import com.unify.pipeline.file.{FileReader, FileWriter}
import com.unify.pipeline.schema.{Pipeline, UnifyConfig}
import com.unify.pipeline.transform.Transform
import org.apache.spark.sql.{DataFrame, SparkSession}

trait PartialJob extends BaseJob {
  /**
   * Reads data via config
   *
   * @param config UnifyConfig
   * @return Dataframe
   */
  override def extract(spark: SparkSession, config: UnifyConfig): DataFrame =
    FileReader.read(spark, config)
  /**
   * Transforms data as per config
   *
   * @param dataFrame Dataframe
   * @param config    UnifyConfig
   * @return
   */
  override def transform(dataFrame: DataFrame, config: Pipeline.Payload): DataFrame = {
    val columnDefinition = Transform(config)
    dataFrame.select(columnDefinition: _*)
  }
  /**
   * Writes data to target as per config
   *
   * @param dataFrame dataframe
   * @param config    UnifyConfig
   * @return
   */
  override def load(dataFrame: DataFrame, config: UnifyConfig): Unit =
    FileWriter.write(dataFrame, config)
}
