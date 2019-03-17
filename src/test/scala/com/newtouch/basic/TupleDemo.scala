package com.newtouch.basic

object TupleDemo {
  def main(args: Array[String]): Unit = {
    // 简单Tuple
    val t = ("leo", 30)

    // 访问Tuple
    t._1

    // zip操作
    val names = Array("leo", "jack", "mike")
    val ages = Array(30, 24, 26)
    val nameAges = names.zip(ages)
    for ((name, age) <- nameAges) println(name + ": " + age)


  }
}
