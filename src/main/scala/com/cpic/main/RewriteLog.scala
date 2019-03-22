package com.cpic.main

import java.sql.Types
import java.util.Properties

import ch.qos.logback.core.db.dialect.OracleDialect
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.jdbc.{JdbcDialect, JdbcDialects, JdbcType}
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}
/**
  * 将hive日志表实时刷回oracle
  */
object RewriteLog extends App {
  //创建SparkContext
  val conf = new SparkConf().setAppName("Reading from Oracle")
  val sc = new SparkContext(conf)
  val oracleDriverUrl = "jdbc:oracle:thin:@//ip:1521/dbinstance"

  val jdbcMap = Map("url" -> oracleDriverUrl,
    "user" -> "username",
    "password" -> "userpassword",
    "dbtable" -> "table_name",
    "driver" -> "oracle.jdbc.driver.OracleDriver")

  //创建sqlContext用来连接oracle、Hive等，由于HiveContext继承自SQLContext，因此，实例化HiveContext既可以操作Oracle，也可操作Hive
  val sqlContext = new HiveContext(sc)
  //加载oracle表数据
  val jdbcDF = sqlContext.read.options(jdbcMap).format("jdbc").load

  //hive切换test库
  sqlContext.sql("use test")
  //向spark注册一个临时表，在临时表上进行数据操作，提高效率，但需要考虑表的规模，以防出现OOM
  //测试问题，直接从oracle上获取数据，当数据量较大时，集群计算时间就会显著增长。
  jdbcDF.registerTempTable("temp_table1")

  val noTotalId = sqlContext.sql("some sql")
  //    println("NO_TOTAL_ID_TMP Total count" + noTotalId.count())
  //    //注册NO_TOTAL_ID，以备下面计算使用
  noTotalId.registerTempTable("temp_table2")
  //未经过优化，后续会考虑加入分区、buket等优化策略，提高效率
  val results = sqlContext.sql(" some sql")
  // Drop temp tables registered to Spark
  sqlContext.dropTempTable("temp_table1")
  sqlContext.dropTempTable("temp_table2")

  results.registerTempTable("resultDF")
  //write to Hive
  sqlContext.sql("insert into ods_incom_biaoma select * from resultDF")

  //Read results from Hive can make the task efficient
  val df2Oracle = sqlContext.sql("select * from ods_incom_biaoma")

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
      //        case DecimalType.Fixed(precision, scale) => Some(JdbcType("NUMBER(" + precision + "," + scale + ")", java.sql.Types.NUMERIC))
      case DecimalType.Unlimited => Some(JdbcType("NUMBER(38,4)", java.sql.Types.NUMERIC))
      case _ => None
    }
  }

  //Registering the OracleDialect
  JdbcDialects.registerDialect(OracleDialect)

  val connectProperties = new Properties()
  connectProperties.put("user", "GSPWJC")
  connectProperties.put("password", "GSPWJC")
  Class.forName("oracle.jdbc.driver.OracleDriver").newInstance()

  //write back Oracle
  //Note: When writing the results back orale, be sure that the target table existing
  JdbcUtils.saveTable(df2Oracle, oracleDriverUrl, "ods_incom_biaoma", connectProperties)

  sc.stop()
}
private case object OracleDialect extends JdbcDialect {
  override def canHandle(url: String): Boolean = url.startsWith("jdbc:oracle")
  override def getCatalystType(
                                sqlType: Int, typeName: String, size: Int, md: MetadataBuilder): Option[DataType] = {
    // Handle NUMBER fields that have no precision/scale in special way
    // because JDBC ResultSetMetaData converts this to 0 procision and -127 scale
    // For more details, please see
    // https://github.com/apache/spark/pull/8780#issuecomment-145598968
    // and
    // https://github.com/apache/spark/pull/8780#issuecomment-144541760
    if (sqlType == Types.NUMERIC && size == 0) {
      // This is sub-optimal as we have to pick a precision/scale in advance whereas the data
      //  in Oracle is allowed to have different precision/scale for each value.
      Option(DecimalType(DecimalType.MAX_PRECISION, 10))
    } else {
      None
    }
  }
}