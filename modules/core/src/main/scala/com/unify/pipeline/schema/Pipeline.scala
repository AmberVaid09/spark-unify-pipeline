package com.unify.pipeline.schema

object Pipeline {

  case class Payload(
                      name: String,
                      source: Storage,
                      target: Storage,
                      columns: Seq[ColumnMetaData])

  case class Storage(
                      path: String,
                      fileFormat: String,
                      partition: Option[String])

  case class ColumnMetaData(
                             sourceName: String,
                             targetName: Option[String],
                             targetType: Option[String],
                             transformation: Option[RuleEngine])

  sealed trait RuleEngine

  case object ToUpper extends RuleEngine

  case object ToLower extends RuleEngine

  case class RegexExp(pattern: String, replacement: String) extends RuleEngine

}
