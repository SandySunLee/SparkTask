package com.cpic.utils
import sys.process._
import org.slf4j.LoggerFactory
object ThreadUtil {
  private val logger = LoggerFactory.getLogger(this.getClass)
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
  def exeShell(command:ProcessBuilder):String={
    var result=""
    try {
      command.!!
    }catch {
      case _:Exception => logger.error("ShellException")
    }
    result
  }

  /**
    * 执行shell脚本
    * @param command
    * @return
    */
  def exeShell(command:String)={
    try {
      (command).!
    }catch {
      case _:Exception => logger.error("ShellException")
    }
  }

  /**
    * 对文件赋权
    * @param file
    * @return
    */
  def chmod(file:String)={
    try {
      (s"chmod -R 755 ${file}").!
    }catch {
      case _:Exception => logger.error("ThreadException")
    }
  }

}
