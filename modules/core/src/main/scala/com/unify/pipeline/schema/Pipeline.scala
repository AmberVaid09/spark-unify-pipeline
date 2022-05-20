package com.unify.pipeline.schema

object Pipeline {

  case class Payload(
                      name: String,
                      source: SourceStorage,
                      target: TargetStorage,
                      columns: Seq[ColumnMetaData])

  case class SourceStorage(
                      path: String,
                      fileFormat: String)

  case class TargetStorage(
                            path: String,
                            fileFormat: String,
                            saveMode : String,
                            partition: Option[String])

  case class ColumnMetaData(
                             sourceName: String,
                             targetName: Option[String],
                             targetType: Option[String],
                             transformation: Option[Seq[RuleEngine]])

  sealed trait RuleEngine

  case object ToUpper extends RuleEngine

  case object ToLower extends RuleEngine

  case class RegexExp(pattern: String, replacement: String) extends RuleEngine

}
