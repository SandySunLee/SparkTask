package com.cpic.utils
import sys.process._
object ThreadUtil {

  /**
    * 判断进程是否存在
    * @param thread
    * @return
    */
  def hasThread(thread:String)={
    ("ps -ef " #| s"grep ${thread}").!!.split("\n").map(LoadSqlFile.remove2Space _)
      .map(_.split(" ")).filter(x=>x(3).toInt!=0).length
  }

}