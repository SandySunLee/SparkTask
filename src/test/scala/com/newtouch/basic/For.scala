package com.newtouch.basic

import scala.collection.mutable.ArrayBuffer

object For {
  def main(args: Array[String]): Unit = {
    //for(i <- 表达式),表达式1 to 10返回一个Range（区间）
    //每次循环将区间中的一个值赋给i
    for (i <- 1 to 10)  //到10
      println(i)

    for (i<- 1 until 10) //不到10
      println(i)


    //增强for循环 for(i <- 数组)
    val arr = Array("a", "b", "c")
    for (i <- arr)
      println(i)

    //高级for循环（守卫）
    //每个生成器都可以带一个条件，注意：if前面没有分号
    for(i <- 1 to 3; j <- 1 to 3 if i != j)
      print((10 * i + j) + " ")
    println()

    //for推导式：如果for循环的循环体以yield开始，则该循环会构建出一个集合
    //每次迭代生成集合中的一个值
    val v = for (i <- 1 to 10) yield i * 10
    println(v)

    //循环终止
    import scala.util.control.Breaks._
    var flag=0
    breakable{
      while (true){
        if (flag >100) break
          else flag+=1
      }
    }



  }

}
