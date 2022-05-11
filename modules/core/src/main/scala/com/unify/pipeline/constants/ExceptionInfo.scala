package com.unify.pipeline.constants

object ExceptionInfo {

  class IncorrectConfig(message : String) extends IllegalArgumentException(s"IncorrectConfig :- $message")

}
