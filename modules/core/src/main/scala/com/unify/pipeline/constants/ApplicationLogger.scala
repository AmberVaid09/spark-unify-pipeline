package com.unify.pipeline.constants

import org.slf4j.{Logger, LoggerFactory}

/**
 * Trait to add logging
 */
trait ApplicationLogger {
  protected lazy val logger: Logger = LoggerFactory.getLogger(getClass.getName)
}