package com.unify.pipeline

import com.unify.pipeline.file.{FileReader, FileWriter}
import com.unify.pipeline.test.util.{DefaultConfig, SparkSessionBaseSpec}

class FileOutputSpec extends SparkSessionBaseSpec with DefaultConfig {

  "FileReader" should "read data via config" in {
    val df = FileReader.read(spark, unifyConfig)
    val bool = try {
      FileWriter.write(df, unifyConfig)
      true
    } catch {
      case _: Exception => false
    }
    assert(bool)
  }


}
