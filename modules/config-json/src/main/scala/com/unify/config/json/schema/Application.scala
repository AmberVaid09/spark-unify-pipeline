package com.unify.config.json.schema

object Application {
  type mapString = Map[String, String]
  type mapOfMapString = Map[String, Map[String, String]]

  case class Config(
                     sparkOptions: Option[mapString],
                     cloudAlias: Option[mapOfMapString]
                   )

}
