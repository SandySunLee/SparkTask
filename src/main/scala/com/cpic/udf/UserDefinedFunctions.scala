package com.cpic.udf

import java.time.ZoneId
import java.util.{Calendar, Date, TimeZone}

import com.cpic.utils.{DateUtils, Dates}
//import org.apache.spark.sql.SQLContext
//import org.apache.spark.sql.hive.HiveContext
//import org.apache.spark.{SparkConf, SparkContext}

/**
  * 自定义UDF函数
  **/
object UserDefinedFunctions {

  def sysdate = {
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

  def interval(date: String, value:String,set:String)={
    var result=""
    val set1=set.trim.toUpperCase
    if(set1.equals("YEAR")) result=Dates.addYear(date,value.toInt)
    if(set1.equals("MONTH")) result=Dates.addYear(date,value.toInt)
    if(set1.equals("DAY")) result=Dates.addYear(date,value.toInt)
    if(set1.equals("HOUR")) result=Dates.addYear(date,value.toInt)
    if(set1.equals("MINUTE")) result=Dates.addYear(date,value.toInt)
    if(set1.equals("SECOND")) result=Dates.addYear(date,value.toInt)
    if(set1.contains("TO")){
      val split=set1.split("(\\s)+TO(\\s)+")
    }

    result
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
      val year = idCard.substring(6).substring(0, 4).toInt
      // 得到年份
      val month = idCard.substring(10).substring(0, 2).toInt // 得到月份
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
    }
    (sex, age)
  }

  /**
    * 模仿oracle trunc函数处理日期
    * @param date
    * @param format
    * @return
    */
  def trunc(date:String,format:String):String={
    var result:String=""
    val format1=format.trim.toUpperCase
    //返回最近第一天
    if(format1.equals("YY") || format1.equals("YEAR") || format1.equals("SYEAR") || format1.equals("Y") || format1.equals("YYY") || format1.equals("YYYY") ) result=Dates.getYearFirstDay(date).substring(0,10)
    //返回最近第一天
    if(format1.equals("MM") || format1.equals("MON") || format1.equals("MONTH") || format1.equals("RM") ) result=Dates.getMonthFirstDay(date).substring(0,10)
    //返回最近0点日期
    if(format1.equals("DD") || format1.equals("J")) result=Dates.parseDate(date).substring(0,10)
    //返回最近季度日期
    if(format1.equals("Q")) result=Dates.getQuarterFirstDay(date).substring(0,10)
    //返回最近周日日期
    if(format1.equals("D") || format1.equals("DY") || format1.equals("DAY") ) result=Dates.getWeekFirstDay(date).substring(0,10)
    result
  }

  /**
    * 拉取某个field的数据
    * @param date
    * @param format
    * @return
    */
  def extract(date:String,field:String):String={
    val date1=Dates.parseDate(date)
    var result=""
    if(field.trim.toUpperCase.equals("YEAR")) result= date1.substring(0,4)
    if(field.trim.toUpperCase.equals("MONTH")) result= date1.substring(5,7)
    if(field.trim.toUpperCase.equals("DAY")) result= date1.substring(8,10)
    if(field.trim.toUpperCase.equals("HOUR")) result= date1.substring(11,13)
    if(field.trim.toUpperCase.equals("MINUTE")) result= date1.substring(14,16)
    if(field.trim.toUpperCase.equals("SECOND")) result= date1.substring(17,19)
    result
  }

  /**
    * 模仿oracle trunc函数处理数字
    * @param number
    * @param decimals
    * @return
    */
  def trunc(number:Double,decimals:Int=0):Double={
    var result=""
    if(decimals>0) {
      val num=number.toString.split("\\.")
      if(decimals>=num(1).length){
        result=number.toString
      }else{
        result=num(0)+"."+num(1).substring(0,decimals)
      }
    }else if(decimals==0) {
      result=number.toInt.toString
    }else {
      val num=number.toString.split("\\.")
      if(decimals.abs>=num(0).length){
        result="0"
      }else{
        result=num(0).substring(0,num(0).length+decimals)+"0"*decimals.abs
      }
    }
    result.toDouble
  }

  /**
    * 字符串首字母大写，其它小写，空格不变
    * initcap('a aBc ab1ab') => A Abc Ab1ab
    * @param str
    * @return
    */
  def initcap(str:String):String={
    val alphabet=str.split("\\W").filter(_.length!=0).map(upperLower _)
    val nonalphabet=str.split("\\w+").filter(_.length!=0)
    var result=""
    val verify=str.substring(0,1)
    if("W".equals(verify.replaceAll("\\W","W"))){//非字母打头
      if(nonalphabet.length>alphabet.length){
        for(i <- 0 until nonalphabet.length){
          if(i!=nonalphabet.length-1){
            result += nonalphabet(i)+alphabet(i)
          }else{
            result += nonalphabet(i)
          }
        }
      }else if(nonalphabet.length==alphabet.length){
        for(i <- 0 until nonalphabet.length){
          result += nonalphabet(i)+alphabet(i)
        }
      }
    }else{//字母打头
      if(nonalphabet.length<alphabet.length){
        for(i <- 0 until alphabet.length){
          if(i!=alphabet.length-1){
            result += alphabet(i)+nonalphabet(i)
          }else{
            result += alphabet(i)
          }
        }
      }else if(nonalphabet.length==alphabet.length){
        for(i <- 0 until alphabet.length){
          result += alphabet(i)+nonalphabet(i)
        }
      }
    }
    result
  }
  //纯字母字符串首字母大写其它小写
  def upperLower(str:String):String={
    val chars=str.toCharArray.map(x=>x.toString)
    var result=""
    for(i <- 0 until chars.length){
      if(i==0){
        result += chars(i).toUpperCase
      }else{
        result += chars(i).toLowerCase()
      }
    }
    result
  }

  /**
    * 将数字转换成字符
    * @param num
    * @return
    */
  def chr(num:Int)={
    num.toChar
  }

  /**
    * 转换时区
    * NEW_TIME("2019-03-22 20:05:10","EST","GMT+8")
    * 大西洋标准时间：AST或ADT
    * 阿拉斯加_夏威夷时间：HST或HDT
    * 英国夏令时：BST或BDT
    * 美国山区时间：MST或MDT
    * 美国中央时区：CST或CDT
    * 新大陆标准时间：NST
    * 美国东部时间：EST或EDT
    * 太平洋标准时间：PST或PDT
    * 格林威治标准时间：GMT
    * Yukou标准时间：YST或YDT
    * @param date
    * @param the
    * @param that
    * @return
    */
  def new_time(date:String,the:String,that:String):String={
    Dates.defaultFormat.format(new Date(Dates.defaultFormat.parse(Dates.parseDate(date)).getTime-TimeZone.getTimeZone(the).getRawOffset()+TimeZone.getTimeZone(that).getRawOffset()))
  }

  /**
    * 获取所在的时区
    * @param date
    * @return
    */
  def dbtimezone:String={
    ZoneId.systemDefault.getId
//    val cal = Calendar.getInstance()
//    val offset = cal.get(Calendar.ZONE_OFFSET)
//    cal.add(Calendar.MILLISECOND, -offset)
//    val timeStampUTC = cal.getTimeInMillis()
//    val timeStamp = System.currentTimeMillis()
//    ((timeStamp - timeStampUTC) / (1000 * 3600)).toString
  }

  /**
    * 获取所在时区
    * @return
    */
  def sessiontimezone:String={
    DateUtils.getZone
  }



  //注册demo
  //  val conf=new SparkConf()
  //  val sc=new SparkContext(conf)
  //  val hiveContext=new HiveContext(sc)
  //  hiveContext.udf.register("addYear",addYear _)
}
