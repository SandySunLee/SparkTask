package com.cpic.utils

import java.io.File
import com.cpic.constant.PublicConstant
import sys.process._
import scala.io.Source

object Level2Level {
  //校验的标记文件
  val flagPath=PublicConstant.idsrpt_home+File.separator+"logs"+File.separator
  def verify(level:String,flag:String="daily")={
    var allshell:String=""
    var levelpath:String=""
    level match {
      case "veidoo" => allshell=PublicConstant.veidoo
      case "edw" => allshell=PublicConstant.edw
      case "index" => allshell=PublicConstant.index
      case "report" => allshell=PublicConstant.report
    }
    level match {
      case "veidoo" => levelpath="cx_"+level
      case "edw" => levelpath="cx_"+level
      case "index" => levelpath="cx_"+level
      case "report" => levelpath="cx_"+level
    }
    val flagFile=flagPath+flag+".flag"
    val myflag=Source.fromFile(flagFile).mkString.split(",")
    val rightflag=allshell.split(",").toBuffer
    // TODO: 只有进程停掉了、标记文件有成功标识才算成功
    //清掉已经执行过的脚本
    for(i<- 0 until rightflag.length){
      for (j<- 0 until myflag.length){
        if(rightflag(i)==myflag(j)){
          rightflag.remove(i)
        }
      }
    }
    //重启未执行的脚本
    //钉钉告警
    for(i <- 0 until rightflag.length){
      val shell=PublicConstant.idsrpt_home+File.separator+levelpath+File.separator+rightflag(i)
      shell.!!
    }
    //重启过的程序继续报错则停掉程序

  }

}
