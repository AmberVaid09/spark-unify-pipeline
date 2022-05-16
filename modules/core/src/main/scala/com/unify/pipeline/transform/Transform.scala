package com.unify.pipeline.transform

import com.unify.pipeline.schema.Pipeline._
import org.apache.spark.sql.Column
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object Transform {

  def apply(config: Payload): Seq[Column] =
    config.columns.map { column =>
      StandardizeTransformation(column).getDefinition
    }

  case class StandardizeTransformation(column: ColumnMetaData) {
    /**
     * @return Column : definition which can be used in select statement
     */
    def getDefinition: Column =
      transformedColumn.cast(getDataType).alias(getTargetName)

    /**
     * @return Column : convert column name to col
     */
    def getColumn: Column = col(column.sourceName)

    /**
     * @return String : gets target column name
     */
    def getTargetName: String = column.targetName.getOrElse(column.sourceName)

    /**
     * @return DataType : gets datatype for column to cast to
     */
    def getDataType: DataType = column.targetType.getOrElse("string").toLowerCase match {
      case "int" | "integer" => IntegerType
      case "long" => LongType
      case "float" => FloatType
      case "double" => DoubleType
      case dataType if dataType.startsWith("decimal") => getDecimalDataType(dataType)
      case "boolean" => BooleanType
      case "date" => DateType
      case "timestamp" => TimestampType
      case _ => StringType
    }

    /**
     * @return Column : Performs transformation on column
     */
    def transformedColumn: Column = column.transformation.getOrElse() match {
      case ToLower => lower(getColumn)
      case ToUpper => upper(getColumn)
      case RegexExp(pattern, replacement) => regexp_replace(getColumn, pattern, replacement)
      case _ => getColumn
    }

    private def getDecimalDataType(dataType: String): DecimalType = {
      val split = dataType.split("\\(")
      try {
        val decimalPrecision = split.last.replace(")", "").split(",")
        DecimalType(decimalPrecision(0).toInt, decimalPrecision(1).toInt)
      } catch {
        case _: Throwable => DecimalType(10, 2)
      }
    }
  }

}
