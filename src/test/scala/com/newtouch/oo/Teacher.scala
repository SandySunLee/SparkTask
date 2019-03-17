package com.newtouch.oo

import scala.reflect.BeanProperty
//java风格的get/set
class Teacher (@BeanProperty var name: String="小明") {
  //Scala中，同样可以在类中定义内部类；但是与java不同的是，每个外部类的对象的内部类，都是不同的类
  class Class(val name: String){}
}
object Teacher extends App{
  val teacher=new Teacher()
  println(teacher.getName)
  for (ele <- Season.values) println(ele)

}
//用object来实现枚举功能
object Season1 extends Enumeration {
  val SPRING, SUMMER, AUTUMN, WINTER = Value
}
//初始化枚举值
object Season extends Enumeration {
  val SPRING = Value(0, "spring")
  val SUMMER = Value(1, "summer")
  val AUTUMN = Value(2, "autumn")
  val WINTER = Value(3, "winter")
}
