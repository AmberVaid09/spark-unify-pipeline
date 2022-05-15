package com.unify.pipeline

import com.unify.pipeline.file.{FileReader, FileWriter}
import com.unify.pipeline.test.util.{DefaultConfig, SparkSessionBaseSpec}

class FileOutputSpec extends SparkSessionBaseSpec with DefaultConfig {

  "FileReader" should "read data via config" in {
    val df = FileReader.read(spark, unifyConfig)
    FileWriter.write(df, unifyConfig)
  }

  "FileReader" should " abc" in {
    val df = FileReader.read(spark, unifyConfig)
    df.repartition(1).write.format("orc").mode("overwrite").save("modules/core/src/test/resources/data/output/file_output_test/")
  }

}
