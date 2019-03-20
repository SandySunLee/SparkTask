package com.cpic.utils

import java.io._
import java.text.SimpleDateFormat
import java.util.Date

import sys.process._
import org.slf4j.LoggerFactory
import scala.io.Source



object Files {
  val logger = LoggerFactory.getLogger(this.getClass)

  /**
    * 修改文件全路径名
    *
    * @param file
    */
  def fixFileName(oldFile: String) = {
    val filePath = new File(oldFile)
    if (!filePath.exists()) {
      logger.warn("文件不存在!")
      null
    }
    if (filePath.isDirectory) {
      logger.warn("输入的是文件夹!")
      null
    }
    val date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date)
    val pro = oldFile.split("\\/").filter(_.trim.length != 0)
    var nf = File.separator
    for (i <- 0 until pro.length) {
      if (i == pro.length - 1) {
        nf += pro(pro.length - 1).split("\\.")(0) + date + "." + pro(pro.length - 1).split("\\.")(1)
      } else {
        nf += pro(i) + File.separator
      }
    }
    filePath.renameTo(new File(nf))
  }

  /**
    * 将全路径源文件拷贝到制定目的地
    *
    * @param sourceFile
    * @param desFile
    */
  def copyFile(sourceFile: String, desFile: String) = {
    val writer = new PrintWriter(new File(desFile + File.separator + getFileName(sourceFile)), "UTF-8")
    val file = Source.fromFile(sourceFile)
    for (i <- file.getLines()) {
      writer.println(i)
    }
    writer.close()
  }

  /**
    * 给制定全路径文件赋权755
    *
    * @param file
    * @return
    */
  def chmodFile(file: String) = {
    (s"chmod -R 755 ${file}").!!
  }

  /**
    * 获取全路径文件的文件名
    *
    * @param file
    * @return
    */
  def getFileName(file: String) = {
    val filePath = new File(file)
    if (!filePath.exists()) {
      logger.warn("文件不存在!")
      null
    }
    if (filePath.isDirectory) {
      logger.warn("输入的是文件夹!")
      null
    }
    filePath.getName
  }

  /**
    * 遍历某个路径下所有子文件
    *
    * @param dir
    * @return
    */
  def getAllFiles(dir: File): Iterator[File] = {
    val children = dir.listFiles().filter(_.isDirectory())
    children.toIterator ++ children.toIterator.flatMap(getAllFiles _)
  }

}

