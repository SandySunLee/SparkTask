package com.newtouch.basic

import java.io.File
import scala.io.Source


class RichFile(val from: File) {
  def read = Source.fromFile(from.getPath).mkString
}

object RichFile {
  //隐式转换方法
  implicit def file2RichFile(from: File) = new RichFile(from)
}
class Man(val name: String)

class Superman(val name: String) {
  def emitLaser = println("emit a laster!")
}

object Man {
  implicit def man2superman(man: Man): Superman = new Superman(man.name)
}

// 案例：考试签到
class SignPen {
  def write(content: String) = println(content)
}

object SignPen extends App{
  implicit val signPen = new SignPen

  def signForExam(name: String)(implicit signPen: SignPen) {
    signPen.write(name + " come to exam in time.")
  }

  println(signForExam("xiaoming"))
}




object ImplicitDemo extends App {
  //导入隐式转换
  import RichFile._
  //import RichFile.file2RichFile
  try {
    println(new File("c://words.txt").read)
  }catch {
    case _:Exception=>println("找不到文件")
  }
  //////////////////////////////////////////////////////////////////////////////////////
//    import implicits.Man._
  val leo = new Man("leo")
  leo.emitLaser

}