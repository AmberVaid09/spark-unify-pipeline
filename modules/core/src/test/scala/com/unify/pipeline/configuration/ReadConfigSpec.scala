package com.unify.pipeline.configuration

import com.unify.pipeline.test.util.BaseSpec

class ReadConfigSpec extends BaseSpec {
  "config" should " be equal to 42 " in {
    assert(config.getInt("number") == 42)
  }

}
