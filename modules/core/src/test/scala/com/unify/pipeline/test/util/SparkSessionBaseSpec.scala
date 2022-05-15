package com.unify.pipeline.test.util

import org.apache.spark.sql.SparkSession

trait SparkSessionBaseSpec extends BaseSpec {

  System.setProperty("hadoop.home.dir", "C:\\Users\\amber\\Downloads\\hadooputils\\hadoop")
  val spark: SparkSession = SparkSession.builder().master("local[*]").getOrCreate()

  override def afterAll(): Unit =
    spark.close()

}
