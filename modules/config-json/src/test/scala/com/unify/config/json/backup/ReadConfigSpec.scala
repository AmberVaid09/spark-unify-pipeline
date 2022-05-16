package com.unify.config.json.backup

import com.unify.pipeline.test.util.BaseSpec
@deprecated
class ReadConfigSpec extends BaseSpec {


  "ReadConfig mapToJSON" should "convert to JSON" in {
    val argMap = ReadConfig.argumentsToMap(Array("config_path=dev.conf", "active_system=some_source_system"))
    assert(ReadConfig.mapToJSON(argMap, "program_args") == "{ program_args : {config_path : dev.conf,active_system : some_source_system} }")
  }

}
