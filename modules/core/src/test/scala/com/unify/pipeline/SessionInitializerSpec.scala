package com.unify.pipeline

import com.unify.pipeline.schema.Application
import com.unify.pipeline.session.SessionInitializer
import com.unify.pipeline.test.util.BaseSpec

class SessionInitializerSpec extends BaseSpec {

  val applicationConfig: Application.Config = Application.Config(
    sparkOptions = Some(Map("spark.master" -> "local", "spark.app.name" -> "unify")),
    cloudAlias = Some(Map("gcp" -> Map("gcp.key" -> "gcp_pass"), "aws" -> Map("aws.key" -> "aws_pass"))))

  "SessionInitializer" should "create and validate spark session" in {
    System.setProperty("hadoop.home.dir", "C:\\Users\\amber\\Downloads\\hadooputils\\hadoop")
    val spark = SessionInitializer.create(applicationConfig)
    assert(spark.sql("select 1 as id").collect().map(_.getInt(0)).head == 1)
  }

  "SessionInitializer" should "create and update updateSparkHadoopOptions" in {
    System.setProperty("hadoop.home.dir", "C:\\Users\\amber\\Downloads\\hadooputils\\hadoop")
    val spark = SessionInitializer.create(applicationConfig)
    // Updating hadoop config to gcp path
    SessionInitializer.updateSparkHadoopOptions(spark,applicationConfig,"gcp")
    assert(spark.sparkContext.hadoopConfiguration.get("gcp.key") == "gcp_pass")

    // Updating hadoop config to aws path
    SessionInitializer.updateSparkHadoopOptions(spark,applicationConfig,"aws")
    assert(spark.sparkContext.hadoopConfiguration.get("aws.key") == "aws_pass")
  }

}
