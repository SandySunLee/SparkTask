package com.cpic.utils

import java.util.Properties

import com.cpic.constant.PublicConstant
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.jdbc.{JdbcDialect, JdbcDialects, JdbcType}
import org.apache.spark.sql.types._
import org.slf4j.LoggerFactory

object OracleUtils {
  val logger = LoggerFactory.getLogger(this.getClass)
//"jdbc:oracle:thin:@//ip:1521/dbinstance"
  val oracleDriverUrl = PublicConstant.oracleDriverUrl
  val username=PublicConstant.ousername
  val password=PublicConstant.opassword
  val driver=PublicConstant.driver
  val otable_name=PublicConstant.otable_name


  /**
    * spark加载oracle某张表
    * @param hc
    * @param otable_name
    * @return
    */
  def loadTable(hc:HiveContext,otable_name:String)={
    val jdbcMap = Map(
      "url" -> oracleDriverUrl,
      "user" -> username,
      "password" -> password,
      "dbtable" -> otable_name,
      "driver" -> driver)
    hc.read.options(jdbcMap).format("jdbc").load
  }

  //overwrite JdbcDialect fitting for Oracle
  val OracleDialect = new JdbcDialect {
    override def canHandle(url: String): Boolean = url.startsWith("jdbc:oracle") || url.contains("oracle")
    //getJDBCType is used when writing to a JDBC table
    override def getJDBCType(dt: DataType): Option[JdbcType] = dt match {
      case StringType => Some(JdbcType("VARCHAR2(255)", java.sql.Types.VARCHAR))
      case BooleanType => Some(JdbcType("NUMBER(1)", java.sql.Types.NUMERIC))
      case IntegerType => Some(JdbcType("NUMBER(16)", java.sql.Types.NUMERIC))
      case LongType => Some(JdbcType("NUMBER(16)", java.sql.Types.NUMERIC))
      case DoubleType => Some(JdbcType("NUMBER(16,4)", java.sql.Types.NUMERIC))
      case FloatType => Some(JdbcType("NUMBER(16,4)", java.sql.Types.NUMERIC))
      case ShortType => Some(JdbcType("NUMBER(5)", java.sql.Types.NUMERIC))
      case ByteType => Some(JdbcType("NUMBER(3)", java.sql.Types.NUMERIC))
      case BinaryType => Some(JdbcType("BLOB", java.sql.Types.BLOB))
      case TimestampType => Some(JdbcType("DATE", java.sql.Types.DATE))
      case DateType => Some(JdbcType("DATE", java.sql.Types.DATE))
      //case DecimalType.Fixed(precision, scale) => Some(JdbcType("NUMBER(" + precision + "," + scale + ")", java.sql.Types.NUMERIC))
      case DecimalType.Unlimited => Some(JdbcType("NUMBER(38,4)", java.sql.Types.NUMERIC))
      case _ => None
    }
  }

  /**
    * 全量数据回写oracle
    * @param df
    * @param tableName
    */
  def saveTable(df:DataFrame,tableName:String)={
    JdbcDialects.registerDialect(OracleDialect)
    val connectProperties = new Properties()
    connectProperties.put("user", username)
    connectProperties.put("password", password)
    Class.forName(driver).newInstance()
    //write back Oracle
    //Note: When writing the results back orale, be sure that the target table existing
    JdbcUtils.saveTable(df, oracleDriverUrl, tableName, connectProperties)
  }

}
