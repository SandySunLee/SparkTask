package com.cpic.udf

import com.cpic.utils.Dates
import org.slf4j.LoggerFactory
//import org.apache.spark.sql.SQLContext
//import org.apache.spark.sql.hive.HiveContext
//import org.apache.spark.{SparkConf, SparkContext}

/**
  * 自定义UDF函数
  **/
object UserDefinedFunctions {
  val logger = LoggerFactory.getLogger(this.getClass)

  def getCurrentDateTime = {
    Dates.getCurrentDateTime
  }

  def getYesterday(date: String): String = {
    Dates.getYesterday(date)
  }

  def getTomorrow(date: String): String = {
    Dates.getTomorrow(date)
  }

  def getMonthFirstDay(date: String): String = {
    Dates.getMonthFirstDay(date)
  }

  def getMonthLastDay(date: String): String = {
    Dates.getMonthLastDay(date)
  }

  def getYearFirstDay(date: String): String = {
    Dates.getYearFirstDay(date)
  }

  def getYearLastDay(date: String): String = {
    Dates.getYearLastDay(date)
  }

  def compare(date1: String, date2: String) = {
    Dates.compare(date1, date2)
  }

  def addDay(date: String, day: Int): String = {
    Dates.addDay(date, day)
  }

  def addMonth(date: String, month: Int): String = {
    Dates.addMonth(date, month)
  }

  def addYear(date: String, year: Int): String = {
    Dates.addYear(date, year)
  }

  /**
    * 比较字符串是否相等
    * oracle将''和null视为相等
    * hive则视为不相等
    * @param str1
    * @param str2
    * @return
    */
  def equal(str1: String, str2: String) = {
    var str11 = ""
    var str22 = ""
    if (str1 == null) str11 = "" else str11 = str1
    if (str2 == null) str22 = "" else str22 = str2
    str11.equals(str22)
  }

  /**
    * 自定义空值函数
    * @param source
    * @param replace
    * @return
    */
  def nvl(source: String, replace: String) = {
    if (source == null || source.equals("")) replace else source
  }

  /**
    * 自定义空值函数2
    * @param source
    * @param replace1
    * @param replace2
    * @return
    */
  def nvl2(source: String, replace1: String, replace2: String) = {
    if (source == null || source.equals("")) replace1 else replace2
  }

  /**
    * 按照pat切分字符串返回数组下标n的值
    * @param str
    * @param pat
    * @param n
    * @return
    */
  def split(str: String, pat: String, n: Int): String = {
    str.split(pat)(n)
  }

  /**
    * 根据身份证号码辨别男女
    * @param idCard
    * @return
    */
  def getGender(idCard: String): Int = {parseId(idCard)._1}

  /**
    * 根据身份证号码获取年龄
    *
    * @param idCard
    * @return
    */
  def getAge(idCard: String): Int = {parseId(idCard)._2}

  def parseId(idCard: String) = {
    var age = 0
    var sex = 0 //性别0为女 1为男 你懂的
    val nian = Dates.getCurrentDate.substring(0, 4).toInt
    val yue = Dates.getCurrentDate.substring(5, 7).toInt
    if (idCard.length == 18) {
      val year = idCard.substring(6).substring(0, 4).toInt;
      // 得到年份
      val month = idCard.substring(10).substring(0, 2).toInt; // 得到月份
      if (idCard.substring(16).substring(0, 1).toInt % 2 == 0) sex = 0 else sex = 1
      if (month <= yue) {
        // 当前月份大于用户出身的月份表示已过生
        age = nian - year + 1
      } else {
        // 当前用户还没过生
        age = nian - year
      }
    } else if (idCard.length == 15) {
      val year = ("19" + idCard.substring(6, 8)).toInt
      // 年份
      val month = (idCard.substring(8, 10)).toInt // 月份
      if (idCard.substring(14, 15).toInt % 2 == 0) sex = 0 else sex = 1
      if (month <= yue) {age = nian - year + 1} else {age = nian - year}
    } else {
      logger.error("输入的身份证号码异常")
    }
    (sex, age)
  }

  //注册demo
  //  val conf=new SparkConf()
  //  val sc=new SparkContext(conf)
  //  val hiveContext=new HiveContext(sc)
  //  hiveContext.udf.register("addYear",addYear _)
}
