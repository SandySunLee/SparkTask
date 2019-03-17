package com.newtouch.basic

object Lazy {
  def main(args: Array[String]): Unit = {
    import scala.io.Source._
    lazy val lines1 = fromFile("C://Users//Administrator//Desktop//test.txt").mkString
    //即使文件不存在，也不会报错，只有第一个使用变量时会报错
    val lines2 = fromFile("C://Users//Administrator//Desktop//test.txt").mkString
    lazy val lines3 = fromFile("C://Users//Administrator//Desktop//test.txt").mkString
    def lines = fromFile("C://Users//Administrator//Desktop//test.txt").mkString


  }

}
