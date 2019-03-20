package com.cpic.main

import java.io.File
import java.util.concurrent.TimeUnit

import org.apache.commons.io.filefilter.HiddenFileFilter
import com.cpic.constant.PublicConstant
import com.cpic.utils.FileListener
import org.apache.commons.io.filefilter.FileFilterUtils
import org.apache.commons.io.monitor.{FileAlterationMonitor, FileAlterationObserver}
import org.slf4j.LoggerFactory


object MoveFile extends App {
  val logger = LoggerFactory.getLogger(this.getClass)
  //循环遍历某个文件夹，一旦有修改则修改镜像路径的文件名再进行copy操作
  // 监控目录
  val rootDir= "/home/" +PublicConstant.readuser
  // 轮询间隔 5 秒
  val interval = TimeUnit.SECONDS.toMillis(1)
  // 创建过滤器
  val directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE)
  val files = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".sh"),FileFilterUtils.suffixFileFilter(".sql"))
  val filter = FileFilterUtils.or(directories, files)
  // 使用过滤器
  val observer = new FileAlterationObserver(new File(rootDir), filter)
  //不使用过滤器
  //        FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir))
  observer.addListener(new FileListener())
  //创建文件变化监听器
  val monitor = new FileAlterationMonitor(interval, observer)
  // 开始监控
  try {
    monitor.start()
  }catch {
    case _:Exception => logger.error("文件夹监控异常!")
  }

}
