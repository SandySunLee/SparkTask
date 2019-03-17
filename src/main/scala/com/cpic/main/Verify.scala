package com.cpic.main

import java.io.File
import com.cpic.constant.PublicConstant
import org.apache.spark.{SparkConf, SparkContext}
import sys.process._
import scala.util.control.Breaks._

/**
  * 本实例功能：解析上游log表判断数据是否加载完成
  */
object Verify extends App{
  //  //返回值是0
  //  val exitCode = "ls -al".!
  //  Seq("ls","-al").!
  //  Process("ls -al").!
  //  //返回值是string
  //  val result = "ls -al" !!
  ////    println(result)
  //  //管道
  ////    ("ls -al" #| "grep doc").!
  //  //重定向
  //  val contents = ("cat" #< new File("/etc/passwd")).!!
  //打印文件
//  ("echo 'success'" #> new File("")).!
  //监控进程
  //("ps -ef " #| "grep spark").!!
  //模拟判断上游数据是否加载完
//  val conf = new SparkConf().setAppName("verify").setMaster("local[2]")
//  val sparkContext = new SparkContext(conf)
  //配置文件读取的需要的表数
  val tablenum=PublicConstant.tab_cnt
  //执行校验的脚本
  val verifyfile=PublicConstant.idsrpt_home+File.separator+"verify.sh"
  //验证不通过睡眠时间
  val sleeptime=PublicConstant.sleeptime.toInt
  def verify(sparkContext:SparkContext)={
    breakable{
      while (true){
        //脚本赋755权限
        val result=sparkContext.parallelize(verifyfile.!!.split(" ")).filter(_.contains("###")).collect()(0).split("###")(1).toInt
        if (result >= tablenum) break
        else Thread.sleep(sleeptime*1000)
      }
    }
  }
  def verify()={
    breakable{
      while (true){
        //脚本赋755权限
        val result=verifyfile.!!.split(" ").filter(_.contains("###")).max.split("###")(1).toInt
        if (result >= tablenum) break
        else Thread.sleep(sleeptime*1000)
      }
    }
  }

}

