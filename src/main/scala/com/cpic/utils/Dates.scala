package com.cpic.utils

import java.text.{SimpleDateFormat}
import java.util.{Calendar, Date}

import org.slf4j.LoggerFactory

/**
  * 所有日期的输出格式必须是hive标准格式
  */
object Dates {
  val logger = LoggerFactory.getLogger(this.getClass)
  /**
    * 10位日期格式
    */
  val DATE_PATTERN = "yyyy-MM-dd"
  /**
    * 8位日期格式
    */
  val LINUX_DATE_PATTERN = "yyyyMMdd"
  /**
    * 标准时间格式：HH:mm:ss
    */
  val TIME_PATTERN = "HH:mm:ss"
  /**
    * 时分：HH:mm
    */
  val SHORT_TIME_PATTERN = "HH:mm"
  /**
    * hive标准日期类型：yyyy-MM-dd HH:mm:ss
    */
  val DATE_TIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN
  /**
    * 标准0秒格式
    */
  val DATE_TIME_PATTERN_TRAIL = "yyyy-MM-dd HH:mm:00"
  /**
    * 16位日期格式
    */
  val DATE_TIME_HIVE = "yyyyMMddHHmmss"
  /**
    * 一天的毫秒数
    */
  val MILLISECONDS_DAY = 1000 * 3600 * 24
  /**
    * 默认日期格式类
    */
  val defaultFormat = new SimpleDateFormat(DATE_TIME_PATTERN)

  /**
    * 获取当前日期时间
    *
    * @return
    */
  def getCurrentDateTime = {
    defaultFormat.format(new Date)
  }

  /**
    * 获取当前日期
    *
    * @return
    */
  def getCurrentDate = {
    new SimpleDateFormat(DATE_PATTERN).format(new Date)
  }

  /**
    * 获取当前时间
    *
    * @return
    */
  def getCurrentTime = {
    new SimpleDateFormat(TIME_PATTERN).format(new Date)
  }

  /**
    * 将任意格式的日期字符串转换成标准格式
    *
    * @param date
    * @return
    */
  def parseDate(date: String): String = {
    var result = ""
    if (date.trim.isEmpty || date == null) {
      result = null
      logger.warn(s"输入的${date}为空")
    }
    try {
      //带.的是有毫秒的
      if (date.contains(".")) {
        val pre = date.trim.split("\\.")(0).split("\\D")

        val pos = date.trim.split("\\.")(1)
        if (pre.length == 3) {
          result = ("00" + pre(0)).reverse.substring(0, 4).reverse + "-" + ("0" + pre(1)).reverse.substring(0, 2).reverse + "-" + ("0" + pre(2)).reverse.substring(0, 2).reverse + " 00:00:00"
        } else if (pre.length == 6) {
          result = ("00" + pre(0)).reverse.substring(0, 4).reverse + "-" + ("0" + pre(1)).reverse.substring(0, 2).reverse + "-" + ("0" + pre(2)).reverse.substring(0, 2).reverse + " " +
            ("0" + pre(3)).reverse.substring(0, 2).reverse + ":" + ("0" + pre(4)).reverse.substring(0, 2).reverse + ":" + ("0" + pre(5)).reverse.substring(0, 2).reverse
        } else if(pre.length == 1){
          if(pre(0).length==14){
            result=pre(0).substring(0,4)+"-"+pre(0).substring(4,6)+"-"+pre(0).substring(6,8)+" "+pre(0).substring(8,10)+":"+pre(0).substring(10,12)+":"+pre(0).substring(12,14)
          }else if(pre(0).length==8){
            result=pre(0).substring(0,4)+"-"+pre(0).substring(4,6)+"-"+pre(0).substring(6,8)+" 00:00:00"
          }else{
            logger.error("日期格式异常")
          }
        }else {
          logger.error("日期格式异常")
        }
      } else {
        val pre = date.trim.split("\\D")
        if (pre.length == 3) {
          result = ("00" + pre(0)).reverse.substring(0, 4).reverse + "-" + ("0" + pre(1)).reverse.substring(0, 2).reverse + "-" + ("0" + pre(2)).reverse.substring(0, 2).reverse + " 00:00:00"
        } else if (pre.length == 6) {
          result = ("00" + pre(0)).reverse.substring(0, 4).reverse + "-" + ("0" + pre(1)).reverse.substring(0, 2).reverse + "-" + ("0" + pre(2)).reverse.substring(0, 2).reverse + " " +
            ("0" + pre(3)).reverse.substring(0, 2).reverse + ":" + ("0" + pre(4)).reverse.substring(0, 2).reverse + ":" + ("0" + pre(5)).reverse.substring(0, 2).reverse
        } else if(pre.length == 1){
          if(pre(0).length==14){
            result=pre(0).substring(0,4)+"-"+pre(0).substring(4,6)+"-"+pre(0).substring(6,8)+" "+pre(0).substring(8,10)+":"+pre(0).substring(10,12)+":"+pre(0).substring(12,14)
          }else if(pre(0).length==8){
            result=pre(0).substring(0,4)+"-"+pre(0).substring(4,6)+"-"+pre(0).substring(6,8)+" 00:00:00"
          }else{
            logger.error("日期格式异常")
          }
        }else {
          logger.error("日期格式异常")
        }
      }
    } catch {
      case _: Exception => logger.error("日期格式异常")
    }
    var date1: Date = null
    try {
      date1 = Dates.defaultFormat.parse(result)
    } catch {
      case _: Exception => logger.error("ParseException")
    }
    Dates.defaultFormat.format(date1)
  }

  /**
    * 获取昨天的日期
    * @param date
    * @return
    */
  def getYesterday(date: String): String = {
    val date1 = parseDate(date).split("\\D")
    var date2=date1(0)+"-"+date1(1)+"-"+(date1(2).toInt-1)+" "+date1(3)+":"+date1(4)+":"+date1(5)
    parseDate(date2)
  }

  /**
    * 获取明天的日期
    * @param date
    * @return
    */
  def getTomorrow(date: String): String = {
    val date1 = parseDate(date).split("\\D")
    var date2=date1(0)+"-"+date1(1)+"-"+(date1(2).toInt+1)+" "+date1(3)+":"+date1(4)+":"+date1(5)
    parseDate(date2)
  }

  /**
    * 获取当月第一天的日期
    * @param date
    * @return
    */
  def getMonthFirstDay(date:String):String={
    val date1=parseDate(date).split("\\D")
    date1(0)+"-"+date1(1)+"-01"+" "+date1(3)+":"+date1(4)+":"+date1(5)
  }

  /**
    * 获取当年第一天的日期
    * @param date
    * @return
    */
  def getYearFirstDay(date:String):String={
    val date1=parseDate(date).split("\\D")
    date1(0)+"-01-01"+" "+date1(3)+":"+date1(4)+":"+date1(5)
  }

  /**
    * 获取当月最后一天的日期
    * @param date
    * @return
    */
  def getMonthLastDay(date:String):String={
    val date1=parseDate(date)
    val calendar=Calendar.getInstance()
    calendar.setTime(defaultFormat.parse(date1))
    val day=calendar.getActualMaximum(Calendar.DATE)
    val date2=date1.split("\\D")
    date2(0)+"-"+date2(1)+"-"+day+" "+date2(3)+":"+date2(4)+":"+date2(5)
  }

  /**
    * 获取当年最后一天的日期
    * @param date
    * @return
    */
  def getYearLastDay(date:String):String={
    val date1=parseDate(date).split("\\D")
    date1(0)+"-12-31 "+date1(3)+":"+date1(4)+":"+date1(5)
  }

  /**
    * 比较两个日期相差的天数
    * @param date1
    * @param date2
    * @return
    */
  def compare(date1:String,date2:String)={
    (defaultFormat.parse(parseDate(date1)).getTime-defaultFormat.parse(parseDate(date2)).getTime).toDouble/MILLISECONDS_DAY
  }

  /**
    * 某日期加/减多少天的日期
    * @param date
    * @param day
    * @return
    */
  def addDay(date: String,day:Int): String = {
    val date1 = parseDate(date).split("\\D")
    var date2=date1(0)+"-"+date1(1)+"-"+(date1(2).toInt+day)+" "+date1(3)+":"+date1(4)+":"+date1(5)
    parseDate(date2)
  }

  /**
    * 某日期加/减多少月的日期
    * @param date
    * @param month
    * @return
    */
  def addMonth(date: String,month:Int): String = {
    val date1 = parseDate(date).split("\\D")
    var date2=date1(0)+"-"+(date1(1).toInt+month)+"-"+date1(2)+" "+date1(3)+":"+date1(4)+":"+date1(5)
    parseDate(date2)
  }

  /**
    * 某日期加/减多少年的日期
    * @param date
    * @param month
    * @return
    */
  def addYear(date: String,year:Int): String = {
    val date1 = parseDate(date).split("\\D")
    var date2=(date1(0).toInt+year)+"-"+date1(1)+"-"+date1(2)+" "+date1(3)+":"+date1(4)+":"+date1(5)
    parseDate(date2)
  }

}

















