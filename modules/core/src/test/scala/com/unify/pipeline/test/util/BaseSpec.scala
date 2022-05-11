package com.unify.pipeline.test.util

import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec

trait BaseSpec extends AnyFlatSpec with BeforeAndAfterAll {

  val config: Config = ConfigFactory.load()

}
