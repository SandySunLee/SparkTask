package com.cpic.main
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import com.cpic.constant.PublicConstant
import org.apache.spark.sql.hive.HiveContext
import org.slf4j.LoggerFactory

import sys.process._
object TransforFile {
  val logger = LoggerFactory.getLogger(this.getClass)
  val transforFile=PublicConstant.idsrpt_home+File.separator+"transforFile.sh"
  val mydb=PublicConstant.mydb
  //下发日志表名
  val logtable="idsrpt_transe_log"
  //下发次数
  var num=0
  /**
    * 执行下发脚本
    * @param tableName
    * @return
    */
  def exeTrans(tableName:String)={
    val curruntTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
    curruntTime+(transforFile+" "+tableName).!!
  }

  /**
    * 校验下发日志
    * @param str
    * @return
    */
  def verify(str:String,hc:HiveContext)={
    val flag=str.split(" ").filter(_.contains("successfully")).length
    val curruntTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
    val starttime="".substring(0,19)
    var sql:String=""
    if(flag==1){
      sql=s"insert into table $mydb.${logtable} select ${num},${starttime},${curruntTime},${logtable},success"
    }else if(flag==0){
      sql=s"insert into table $mydb.${logtable} select ${num},${starttime},${curruntTime},${logtable},failure"
      logger.error("下发失败!")
    }else {
      logger.warn("下发异常!")
    }
    num+=1
    hc.sql(sql)
  }

}
