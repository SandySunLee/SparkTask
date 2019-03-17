package com.newtouch.basic

object Var {
  def main(args: Array[String]): Unit = {
    //不可变变量
    val a=1
    //可变变量
    var b=2.0
    //指定数据类型
    val c:String="shabi"
    //Scala和Java一样，有7种数值类型Byte、Char、Short、Int、Long、Float和Double（无包装类型）和一个Boolean类型

    //aply()
    //使用“类名()”的形式，其实就是“类名.apply()”的一种缩写
    val arr=Array(1).foreach(println(_))
    //不同于
    val arrErr=new Array[Int](1).foreach(println(_))

  }

}
