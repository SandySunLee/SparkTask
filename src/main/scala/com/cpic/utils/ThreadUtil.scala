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

  /**
    * 执行shell命令
    * @param command
    * @return
    */
  def exeShell(command:String):String={
    (command).!!
  }

  /**
    * 对文件赋权
    * @param file
    * @return
    */
  def chmod(file:String)={
    (s"chmod -R 755 ${file}").!
  }

}
