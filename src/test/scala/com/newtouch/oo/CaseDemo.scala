package com.newtouch.oo

import scala.util.Random

object CaseDemo extends App {
  //匹配字符串
  val arr0 = Array("YoshizawaAkiho", "YuiHatano", "AoiSola")
  val name = arr0(Random.nextInt(arr0.length))
  name match {
    case "YoshizawaAkiho" => println("吉泽老师...")
    case "YuiHatano" => println("波多老师...")
    case _ => println("真不知道你们在说什么...")
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //匹配类型
  val arr1 = Array("hello", 1, 2.0, CaseDemo)
  val v = arr1(Random.nextInt(4))
  println(v)
  v match {
    case x: Int => println("Int " + x)
    case y: Double if(y >= 0) => println("Double "+ y)
    case z: String => println("String " + z)
    case _ => throw new Exception("not match exception")
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //匹配数组、元祖
  val arr2 = Array(1, 3, 5)
  arr2 match {
    case Array(1, x, y) => println(x + " " + y)
    case Array(0) => println("only 0")
    case Array(0, _*) => println("0 ...")
    case _ => println("something else")
  }

  val lst = List(3, -1)
  lst match {
    case 0 :: Nil => println("only 0")
    case x :: y :: Nil => println(s"x: $x y: $y")
    case 0 :: tail => println("0 ...")
    case _ => println("something else")
  }

  val tup = (2, 3, 7)
  tup match {
    case (1, x, y) => println(s"1, $x , $y")
    case (_, z, 5) => println(z)
    case  _ => println("else")
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //样例类匹配
  val arr3 = Array(CheckTimeOutTask, HeartBeat(12333), SubmitTask("0001", "task-0001"))

  arr3(Random.nextInt(arr3.length)) match {
    case SubmitTask(id, name) => {
      println(s"$id, $name")//前面需要加上s, $id直接取id的值
    }
    case HeartBeat(time) => {
      println(time)
    }
    case CheckTimeOutTask => {
      println("check")
    }
  }

}

case class SubmitTask(id: String, name: String)
case class HeartBeat(time: Long)
case object CheckTimeOutTask
