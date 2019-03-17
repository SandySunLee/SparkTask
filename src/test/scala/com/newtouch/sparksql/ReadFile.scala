package com.newtouch.sparksql

import java.io.FileNotFoundException

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source
import scala.util.control.Breaks._
//读取sql文件解析出空格、制表符、分号、注释、入参
// TODO: 单行注释需要优化
object ReadSql extends App{
  val filePath:String="D:\\myjava\\test.sql"
  val mydb="idsrpt"
  val autodb="dmgr"
  val workdate:String="20180101"
  //scala读取本地文件->过滤空格、制表符、回车、sql末尾的;->过滤注释语句->存入数组
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
    case e1:FileNotFoundException => println("系统找不到指定的文件。")
    case _:Exception => println("莫名的原因")
  }


  println(sql.toBuffer)


  //去掉空格和制表符
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
