package com.cpic.utils

import java.io.FileNotFoundException

import org.apache.spark.sql.hive.HiveContext
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.control.Breaks.{break, breakable}

object LoadSqlFile {
  val logger = LoggerFactory.getLogger(this.getClass)
  def exeSql(arr:Array[String],hc:HiveContext)={
    for(line<- arr){
      logger.info(line)
      try {
        hc.sql(line)
      }catch {
        case _:Exception => logger.error("sql执行失败!")
      }
    }
  }
  /**
    * 加载一个workdate的sql文件
    * @param filePath
    * @param mydb
    * @param autodb
    * @param workdate
    * @return
    */
  def loadSqlFile(filePath:String,mydb:String,autodb:String,workdate:String): Array[String]={
    var sql: Array[String] = null
    try {
      sql = Source.fromFile(filePath).mkString.split(";")
        .map(x => x.replaceAll("\r|\n|\\s", " "))
        .map(x => remove2Space(x))
        .map(_.replaceAll("--(.*)--",""))
        .map(_.replaceAll("\\/\\*(.*)\\*\\/",""))
        .map(_.replace("${mydb}",s"${mydb}"))
        .map(_.replace("${autodb}",s"${autodb}"))
        .map(_.replace("${workdate}",s"${workdate}"))
        .filter(_.trim.length !=0)
    } catch {
      case e1:FileNotFoundException => logger.error("系统找不到指定的文件!")
      case _:Exception => logger.error("莫名的原因!")
    }
    sql
  }

  /**
    * 加载两个workdate的sql文件
    * @param filePath
    * @param mydb
    * @param autodb
    * @param workdate1
    * @param workdate2
    * @return
    */
  def loadSqlFile(filePath:String,mydb:String,autodb:String,workdate1:String,workdate2:String): Array[String]={
    var sql: Array[String] = null
    try {
      sql = Source.fromFile(filePath).mkString.split(";")
        .map(x => x.replaceAll("\r|\n|\\s", " "))
        .map(x => remove2Space(x))
        .map(_.replaceAll("--(.*)--",""))
        .map(_.replaceAll("\\/\\*(.*)\\*\\/",""))
        .map(_.replace("${mydb}",s"${mydb}"))
        .map(_.replace("${autodb}",s"${autodb}"))
        .map(_.replace("${workdate1}",s"${workdate1}"))
        .map(_.replace("${workdate2}",s"${workdate2}"))
        .filter(_.trim.length !=0)
    } catch {
      case e1:FileNotFoundException => logger.error("系统找不到指定的文件!")
      case _:Exception => logger.error("莫名的原因!")
    }
    sql
  }
  /**
    * 去掉空格和制表符
    * @param str
    * @return
    */
  def remove2Space(str: String): String = {
    var result = str.replaceAll("\t", " ").replaceAll("  ", " ")
    breakable {
      while (true) {
        if (!result.contains("  ") && !result.contains("\t")) break
        else result = result.replaceAll("  ", " ")
      }
    }
    result
  }
}
