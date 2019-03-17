package com.cpic.etl.veidoo

import java.io.File

import com.cpic.constant.PublicConstant
import org.slf4j.LoggerFactory

import scala.actors.Actor
import sys.process._

object Veidoo {
  val logger = LoggerFactory.getLogger(this.getClass)
  val veidoo=PublicConstant.veidoo.split(",")
  def exeVeidoo()={
    Veidoo1.start()
    Veidoo2.start()
    Veidoo3.start()
  }
}

object Veidoo1 extends Actor{
  val logger = LoggerFactory.getLogger(this.getClass)
  val veidoo1=Veidoo.veidoo(0)
  val veidoo1path=PublicConstant.sleeptime+File.separator+"veidoo"+File.separator+veidoo1+".sh"
  override def act(): Unit = {
    s"${veidoo1path}".!
    logger.info(this.getClass.getName)
  }
}
object Veidoo2 extends Actor{
  val logger = LoggerFactory.getLogger(this.getClass)
  val veidoo2=Veidoo.veidoo(1)
  val veidoo2path=PublicConstant.sleeptime+File.separator+"veidoo"+File.separator+veidoo2+".sh"
  override def act(): Unit = {
    s"${veidoo2path}".!
    logger.info(this.getClass.getName)
  }
}
object Veidoo3 extends Actor{
  val logger = LoggerFactory.getLogger(this.getClass)
  val veidoo3=Veidoo.veidoo(2)
  val veidoo3path=PublicConstant.sleeptime+File.separator+"veidoo"+File.separator+veidoo3+".sh"
  override def act(): Unit = {
    s"${veidoo3path}".!
    logger.info(this.getClass.getName)
  }
}