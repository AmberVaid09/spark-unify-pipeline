package com.unify.pipeline

import com.unify.pipeline.file.FileReader
import com.unify.pipeline.test.util.{DefaultConfig, SparkSessionBaseSpec}
import org.apache.spark.sql.DataFrame

class FileInputSpec extends SparkSessionBaseSpec with DefaultConfig {

  private val expectedNames = Array("Dale Holt", "Rhiannon Saunders", "Hu Hopkins", "Marvin Gonzales", "Zena Howe")
  private val validateData = (df: DataFrame) =>
    (df.select("name").collect().map(_.getString(0)) diff expectedNames).length == 0

  "FileReader" should {
    "read data via config" in {
      val df = FileReader.read(spark, unifyConfig)
      assert(validateData(df))
    }

    "read data via params" in {
      val df = FileReader.read(spark, "orc", "modules/core/src/test/resources/data/input/file_read_orc/")
      assert(validateData(df))
    }
  }

}
