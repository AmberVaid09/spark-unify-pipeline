package com.unify.pipeline

import com.unify.pipeline.file.FileReader
import com.unify.pipeline.test.util.{DefaultConfig, SparkSessionBaseSpec}
import com.unify.pipeline.transform.Transform

class TransformationSpec extends SparkSessionBaseSpec with DefaultConfig {

  "transformation" should "work" in {
    val readDf = FileReader.read(spark, unifyConfig)
    val columnList = Transform(pipelinePayload)
    val outputDf = readDf.select(columnList: _*)
    outputDf.show(false)
    outputDf.printSchema()
  }

}
