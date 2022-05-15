package com.unify.pipeline.file

import com.unify.pipeline.schema.UnifyConfig
import org.apache.spark.sql.DataFrame

object FileWriter {
  /** Where's mode ????  */

  /**
   * Write file to a Path based on config
   *
   * @param dataFrame Dataframe
   * @param config    UnifyConfig
   */
  def write(dataFrame: DataFrame, config: UnifyConfig): Unit = {
    val target = config.pipeline.target

    val fileConfigs = config.appConfig.fileIo match {
      case Some(io) => io.output.getOrElse(target.fileFormat, Map.empty[String, String])
      case None => Map.empty[String, String]
    }

    write(dataFrame, target.fileFormat, fileConfigs, target.partition, target.path)
  }

  /**
   * Write file to a Path based on config
   *
   * @param dataFrame   Dataframe
   * @param fileFormat  File format
   * @param fileConfigs file configs
   * @param partitions  partitions
   * @param path        path
   */
  def write(dataFrame: DataFrame, fileFormat: String, fileConfigs: Map[String, String], partitions: Option[String], path: String): Unit =
    dataFrame.write.format(fileFormat).options(fileConfigs).partitionBy(getPartitions(partitions): _*).save(path)

  /**
   * Write file to a Path based on config
   *
   * @param dataFrame   Dataframe
   * @param fileFormat  File format
   * @param partitions  partitions
   * @param path        path
   */
  def write(dataFrame: DataFrame, fileFormat: String, partitions: Option[String], path: String): Unit =
    dataFrame.write.format(fileFormat).partitionBy(getPartitions(partitions): _*).save(path)

  /**
   * Write file to a Path based on config
   *
   * @param dataFrame   Dataframe
   * @param fileFormat  File format
   * @param path        path
   */
  def write(dataFrame: DataFrame, fileFormat: String, path: String): Unit =
    dataFrame.write.format(fileFormat).save(path)


  /**
   * Returns partition for writing
   * @param partition Partitions 
   * @return
   */
  private def getPartitions(partition: Option[String]): Seq[String] =
    partition match {
      case Some(columns) => columns.split(",").toSeq
      case None => Seq.empty[String]
    }

}
