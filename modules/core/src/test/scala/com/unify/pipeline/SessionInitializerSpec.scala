package com.unify.pipeline

import com.unify.pipeline.session.SessionInitializer
import com.unify.pipeline.test.util.BaseSpec

class SessionInitializerSpec extends BaseSpec {

  "SessionInitializer" should "create and validate spark session" in {
    System.setProperty("hadoop.home.dir", "C:\\Users\\amber\\Downloads\\hadooputils\\hadoop")
    val spark = SessionInitializer.create(config)
    assert(spark.sql("select 1 as id").collect().map(_.getInt(0)).head == 1)
  }

}
