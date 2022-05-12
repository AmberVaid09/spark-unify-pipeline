package com.unify.pipeline

import com.unify.pipeline.session.SessionInitializer
import com.unify.pipeline.test.util.BaseSpec

class SessionInitializerSpec extends BaseSpec {

  "SessionInitializer" should "create and validate spark session" in {
    System.setProperty("hadoop.home.dir", "C:\\Users\\amber\\Downloads\\hadooputils\\hadoop")
    val spark = SessionInitializer.create(config)
    assert(spark.sql("select 1 as id").collect().map(_.getInt(0)).head == 1)
  }

  "SessionInitializer" should "create and update updateSparkHadoopOptions" in {
    System.setProperty("hadoop.home.dir", "C:\\Users\\amber\\Downloads\\hadooputils\\hadoop")
    val spark = SessionInitializer.create(config)
    // Updating hadoop config to gcp path
    SessionInitializer.updateSparkHadoopOptions(spark,config,"gcp")
    assert(spark.sparkContext.hadoopConfiguration.get("key") == "gcp_pass")

    // Updating hadoop config to aws path
    SessionInitializer.updateSparkHadoopOptions(spark,config,"aws")
    assert(spark.sparkContext.hadoopConfiguration.get("key") == "aws_pass")
  }

}
