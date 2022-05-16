package com.unify.pipeline.test.util

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, md5, concat_ws}

trait Comparison {

  private val key = "key"

  def compare(actualDataFrame: DataFrame, expectedDataFrame: DataFrame): Boolean = {
    val md5ActualDf = createMD5(actualDataFrame)
    val md5ExpectedDf = createMD5(expectedDataFrame)
    val leftAnti = md5ActualDf.join(md5ExpectedDf, Seq(key), "left_anti")
    val leftSemi = md5ActualDf.join(md5ExpectedDf, Seq(key), "left_semi")
    actualDataFrame.count() == leftSemi.count() && leftAnti.count() == 0
  }

  def createMD5(dataFrame: DataFrame): DataFrame = {
    val columnList = dataFrame.columns.map(col).toSeq
    val concatColumnList = concat_ws(",", columnList:_*)
    val transformation = md5(concatColumnList).alias(key)
    dataFrame.select(transformation)
  }

}
