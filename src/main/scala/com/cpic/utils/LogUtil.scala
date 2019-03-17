package com.cpic.utils

import org.apache.spark.sql.hive.HiveContext

object LogUtil extends App {
  /**
   * **
   *  刷新小日志表
   *
   *  workdate	string	执行日期：yyyymmdd
   * proc_name	string	执行程序
   * proc_step	string	执行步骤
   * loadfromdate	string	加载起期：YYYYMMDD 00:00:00
   * loadenddate	string	加载止期：YYYYMMDD 23:59:59
   * stat_date	string	开始时间
   * end_date	string	结束时间
   * rstatus	string	执行结果：枚举值S 成功 F 失败
   * proc_level	string	层级：枚举值L 宽表层 M 指标层 R 报表层
   *
   */

  def insertLogDtl(mydb: String,hiveContext: HiveContext, workdate: String, proc_name: String, proc_step: String, loadfromdate: String, loadenddate: String, stat_date: String, rstatus: String, proc_level: String): Unit = {
    var strSql = s"""
       insert into table ${mydb}.cx_rstatus_log_dtl select  '${workdate}',
          '${proc_name}','${proc_step}', 
          from_unixtime(unix_timestamp('${loadfromdate}','yyyyMMddHHmmss'),'yyyy-MM-dd HH:mm:ss'),
          from_unixtime(unix_timestamp('${loadenddate}', 'yyyyMMddHHmmss'),'yyyy-MM-dd HH:mm:ss'), 
         '${stat_date}', substr(current_timestamp,1,19) end_time, '${rstatus}','${proc_level}' 
           """
    var entrance = System.currentTimeMillis();
    println(strSql)
    hiveContext.sql(strSql)

    println("日志方法调用时间差----" + (System.currentTimeMillis() - entrance) / 1000 + "秒")
  }

  def insertFirstLogDtl(mydb: String,hiveContext: HiveContext, workdate: String, tabName: String): Unit = {
    var entrance = System.currentTimeMillis();
    var strSql = s""" insert overwrite table ${mydb}.cx_rstatus_log_dtl 
          select * from ${mydb}.cx_rstatus_log_dtl where workdate<> ${workdate}
           or  proc_name<>'${tabName}'
       """
    println(strSql)
    hiveContext.sql(strSql)
    println("日志方法调用时间差----" + (System.currentTimeMillis() - entrance) / 1000 + "秒")
  }

  /**
   * **
   *
   *    *  刷新大日志表 ${mydb}.cx_rstatus_log
   * workdate	string	执行日期
   * proc_name	string	执行程序
   * loadfromdate	string	加载起期：YYYYMMDD 00:00:00
   * loadenddate	string	加载止期：YYYYMMDD 23:59:59
   * stime	string	开始时间
   * endtime	string	结束时间
   * rstatus	string	执行结果
   * proc_level	string	层级
   *
   *
   *
   * select * from ${mydb}.cx_rstatus_log where proc_level='S' order by workdate desc
   */

  def query_cx_rstatus_log_cnt(mydb: String, hiveContext: HiveContext, workdate: String, proc_name: String, proc_level: String): Unit = {
    var strSql = s"""select  * from ${mydb}.cx_rstatus_log where   proc_level='${proc_level}' and  workdate= '${workdate}' order by endtime desc """
//    proc_name in (${proc_name}) and
    var entrance = System.currentTimeMillis();
    println("查询大日志表语句")
    println(strSql)
    hiveContext.sql(strSql).show()
    println(s"查询大日志表语句::---${DateUtils.getCurrentlyTime}-- 日志方法调用时间差---- ${(System.currentTimeMillis() - entrance) / 1000 } 秒")
  }

}