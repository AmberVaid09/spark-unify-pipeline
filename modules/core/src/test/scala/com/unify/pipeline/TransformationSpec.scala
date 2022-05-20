package com.unify.pipeline

import com.unify.pipeline.file.FileReader
import com.unify.pipeline.test.util.{Comparison, DefaultConfig, SparkSessionBaseSpec}
import com.unify.pipeline.transform.Transform

class TransformationSpec extends SparkSessionBaseSpec with DefaultConfig with Comparison {

  "transformation" should {
    "return dataframe with correct output" in {
      val readDf = FileReader.read(spark, unifyConfig)
      val columnList = Transform(pipelinePayload)
      val actualDf = readDf.select(columnList: _*)
      val expectedDf = FileReader.read(spark,
        "orc",
        "modules/core/src/test/resources/data/output/transformation/base_example/")

      assert(compare(actualDf, expectedDf))
    }
  }

}
