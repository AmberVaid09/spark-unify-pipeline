package com.unify.pipeline.configuration

import com.unify.pipeline.constants.ExceptionInfo.IncorrectConfig
import com.unify.pipeline.test.util.BaseSpec

class ReadConfigSpec extends BaseSpec {
  "ReadConfig" should "return Config" in {
    val confPath = "config_validation/config_test.conf"
    val config = ReadConfig(Array(s"config_path=$confPath", "active_system=some_source_system"))
    assert(config.getString("name") == "unify_pipeline" &&
      config.getString("program_arguments.config_path") == confPath &&
      config.getString("program_arguments.active_system") == "some_source_system")
  }

  "ReadConfig" should " work without config path return Config" in {
    val config = ReadConfig(Array("active_system=some_source_system"))
    assert(config.getString("program_arguments.active_system") == "some_source_system")
  }

  "ReadConfig" should " throw exception" in {
    assertThrows[IncorrectConfig] {
      ReadConfig(Array())
    }
  }

  "ReadConfig argumentsToMap" should "convert arguments to map" in {
    val outputData = ReadConfig.argumentsToMap(Array("config_path=dev.conf", "active_system=some_source_system"))
    assert(outputData("config_path") == "dev.conf" && outputData("active_system") == "some_source_system")
  }

  "ReadConfig argumentsToMap" should "throw exception" in {
    assertThrows[IncorrectConfig] {
      ReadConfig.argumentsToMap(Array("config_path"))
    }
  }

  "ReadConfig validateConfigs" should "return true" in {
    val argMap = ReadConfig.argumentsToMap(Array("config_path=dev.conf", "active_system=some_source_system"))
    assert(ReadConfig.validateConfigs(argMap))
  }

  "ReadConfig validateConfigs" should "throw exception" in {
    val argMap = ReadConfig.argumentsToMap(Array("config_path=dev.conf"))
    assertThrows[IncorrectConfig] {
      assert(ReadConfig.validateConfigs(argMap))
    }
  }

  "ReadConfig mapToJSON" should "convert to JSON" in {
    val argMap = ReadConfig.argumentsToMap(Array("config_path=dev.conf", "active_system=some_source_system"))
    assert(ReadConfig.mapToJSON(argMap, "program_args") == "{ program_args : {config_path : dev.conf,active_system : some_source_system} }")
  }

}
