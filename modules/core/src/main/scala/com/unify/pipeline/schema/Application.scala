package com.unify.pipeline.schema

object Application {
  type mapString = Map[String, String]
  type mapOfMapString = Map[String, Map[String, String]]

  case class FileIo(input: mapOfMapString, output: mapOfMapString)

  case class Config(
                     sparkOptions: Option[mapString],
                     cloudAlias: Option[mapOfMapString],
                     fileIo: Option[FileIo]
                   )

}
