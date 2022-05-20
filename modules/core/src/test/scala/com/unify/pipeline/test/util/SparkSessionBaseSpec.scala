package com.unify.pipeline.test.util

import org.apache.spark.sql.SparkSession

trait SparkSessionBaseSpec extends BaseSpec {

  val spark: SparkSession = SparkSession.builder().master("local[*]").getOrCreate()

}
